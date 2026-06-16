package com.hust.soict.ict.aims.services.payment.paypal;

import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.dto.response.payment.paypal.PayPalCreateResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.order.OrderFinalization;
import com.hust.soict.ict.aims.services.order.PlaceOrderService;
import com.hust.soict.ict.aims.services.payment.PaymentMethod;
import com.hust.soict.ict.aims.services.payment.PaymentService;
import com.hust.soict.ict.aims.services.payment.contract.IRedirectPaymentGateway;
import com.hust.soict.ict.aims.services.payment.contract.IRefundCapability;
import com.hust.soict.ict.aims.subsystems.paypal.model.PaymentCapture;
import com.hust.soict.ict.aims.subsystems.paypal.model.PaymentInitiation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

/*
 * [SOLID DIP: low-severity note][cite: 1]
 * Principle: Dependency Inversion (D)[cite: 1]
 * Why: This service correctly depends on the IPaymentProvider abstraction, but[cite: 1]
 *      PlaceOrderService and OrderDraftContext are injected as CONCRETE classes,[cite: 1]
 *      so orchestration is still bound to two concrete in-process types. Severity[cite: 1]
 *      is low because both are stable Spring beans (same reasoning as OrderContro][cite: 1]
 * Proposed Solution: If finer testability is wanted, depend on narrow ports[cite: 1]
 *      (e.g. OrderFinalizer, OrderDraftSource) and inject those instead.[cite: 1]
 */
@Service
@Slf4j
public class PaypalPaymentService extends PaymentService implements IRefundCapability {
    private final IRedirectPaymentGateway paymentProvider;

    public PaypalPaymentService(IRedirectPaymentGateway paymentProvider, OrderDraftContext orderDraftContext, OrderFinalization orderFinalization) {
        super(orderDraftContext, orderFinalization);
        this.paymentProvider = paymentProvider;
    }

    @Override
    public PaymentMethod method() {
        return PaymentMethod.PAYPAL;
    }

    @Override
    protected PaymentTransaction generatePaymentTransaction(String transactionContent) {
        return PaymentTransaction.of(
                transactionContent,
                Instant.now(),
                method().name(),
                currentOrder().getTotalAmount(),   // amount kept in VND — the gateway currency stays in the subsystem
                currentOrder()
        );
    }

    /**
     * Step 1 — create a PayPal payment for the current draft order and return the
     * approval URL the frontend must open.
     */
    public PayPalCreateResponse createPayment() throws PaymentException {
        Order order = currentOrder();
        PaymentInitiation initiation = paymentProvider.createPayment(order.getId().toString(), order.getTotalAmount());

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
        Order order = currentOrder();

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

        log.info("[PayByCreditCardService] PayPal capture {} COMPLETED — finalizing order {}",
                capture.captureId(), order.getId());

        // Crucial integration rule: persist the order + send confirmation email.
        return finalizeOrder(generatePaymentTransaction("PAYPAL-" + capture.captureId()));
    }

    /**
     * Step 2' — customer cancelled on PayPal. Best-effort log; the draft is kept
     * intact so the customer can retry or switch back to VietQR.
     */
    public void cancelPayment() {
        try {
            Order order = currentOrder();
            log.info("[PayByCreditCardService] PayPal payment cancelled for draft order {} — draft kept for retry",
                    order.getId());
        } catch (OrderNotPlacedException e) {
            log.warn("[PayByCreditCardService] Cancel received but no draft order is present in the session");
        }
    }

    @Override
    public void refund(PaymentTransaction transaction) {
        //TODO: REFUND PAYPAL HERE
    }
}
