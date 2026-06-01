package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.dto.response.payments.PayPalCreateResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.order.OrderFinalization;
import com.hust.soict.ict.aims.services.order.PlaceOrderService;
import com.hust.soict.ict.aims.subsystems.paypal.IPaymentProvider;
import com.hust.soict.ict.aims.subsystems.paypal.PaymentCapture;
import com.hust.soict.ict.aims.subsystems.paypal.PaymentInitiation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Owns the credit-card / PayPal payment lifecycle for AIMS.
 *
 * <p>This is the credit-card counterpart of {@code PayOrderService} (VietQR):
 * the single place where a PayPal {@link PaymentTransaction} is created and the
 * order is finalized.
 *
 * <h3>Why this is a refactor of the old {@code PayByCreditCardService}</h3>
 * The previous version (a) re-implemented card validation + a hard-coded
 * "starts-with-4" gateway simulation, (b) duplicated the exact same logic that
 * already lived in {@code PaymentServiceImpl}, (c) fetched the order by id from a
 * request DTO instead of the session draft, and (d) never finalized the order.
 * Those are low cohesion (validation + gateway + persistence + email in one
 * method) and high coupling (two concrete repositories + a notification service).
 *
 * <h3>New design</h3>
 * <ul>
 *   <li><b>SRP / cohesion:</b> orchestrate "create payment" and "capture &amp;
 *       finalize". Nothing else.</li>
 *   <li><b>DIP / low coupling:</b> depends on the {@link IPaymentProvider} facade
 *       (not on PayPal), on {@link OrderDraftContext} (not on a request DTO), and
 *       on {@link PlaceOrderService} (the existing persistence + email owner).</li>
 *   <li><b>Integration rule:</b> on a successful capture it triggers
 *       {@link OrderFinalization#finalizeOrder(Order)} — exactly the
 *       contract the VietQR flow uses.</li>
 * </ul>
 *
 * <h3>Transaction persistence note</h3>
 * Following the proven VietQR path, the {@link PaymentTransaction} is created and
 * handed to {@code finalizeOrder()}, which is the system's single persistence
 * owner (it saves the {@code Order} aggregate, cascading the transaction, and
 * sends the confirmation email). A direct {@code PaymentTransactionRepository.save}
 * here would double-insert. If — and only if — {@code finalizeOrder()} does NOT
 * cascade-persist the transaction in your build, inject
 * {@code PaymentTransactionRepository} and save before finalizing.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayByCreditCardService {

    private final IPaymentProvider paymentProvider;     // PayPal facade (abstraction)
    private final OrderDraftContext orderDraftContext;  // session draft source
    private final OrderFinalization orderFinalization;  // finalize + confirmation email

    /**
     * Step 1 — create a PayPal payment for the current draft order and return the
     * approval URL the frontend must open.
     */
    public PayPalCreateResponse createPayment() throws PaymentException {
        Order order = orderDraftContext.getDraftOrder();
        PaymentInitiation initiation = paymentProvider.createPayment(order);

        log.info("[PayByCreditCardService] PayPal order {} created for AIMS order {}",
                initiation.providerOrderId(), order.getId());

        return new PayPalCreateResponse(initiation.approvalUrl(), initiation.providerOrderId());
    }

    /**
     * Step 2 — capture the approved PayPal payment, record the transaction, and
     * finalize the order (persist + email) via {@link PlaceOrderService}.
     *
     * @param providerOrderId the PayPal token returned to the frontend on redirect
     */
    public OrderResponse capturePayment(String providerOrderId) throws PaymentException {
        Order order = orderDraftContext.getDraftOrder();

        PaymentCapture capture = paymentProvider.capturePayment(providerOrderId);

        if (!capture.completed()) {
            throw new PaymentException(
                    "PayPal payment was not completed. Status: " + capture.status());
        }

        // Defence in depth: the captured order must reference THIS draft order, so a
        // forged/leaked token cannot be used to finalize someone else's order.
        if (!order.getId().toString().equals(capture.referenceId())) {
            throw new PaymentException(
                    "Captured PayPal payment does not belong to the current order.");
        }

        PaymentTransaction transaction = PaymentTransaction.of(
                "PAYPAL-" + capture.captureId(),
                Instant.now(),
                PaymentTransaction.Method.PAYPAL,
                order.getTotalAmount(),   // amount kept in VND — the gateway currency stays in the subsystem
                order
        );

        log.info("[PayByCreditCardService] PayPal capture {} COMPLETED — finalizing order {}",
                capture.captureId(), order.getId());

        // Crucial integration rule: persist the order + send confirmation email.
        return orderFinalization.finalizeOrder(transaction.getOrder());
    }

    /**
     * Step 2' — customer cancelled on PayPal. Best-effort log; the draft is kept
     * intact so the customer can retry or switch back to VietQR.
     */
    public void cancelPayment() {
        try {
            Order order = orderDraftContext.getDraftOrder();
            log.info("[PayByCreditCardService] PayPal payment cancelled for draft order {} — draft kept for retry",
                    order.getId());
        } catch (OrderNotPlacedException e) {
            log.warn("[PayByCreditCardService] Cancel received but no draft order is present in the session");
        }
    }
}
