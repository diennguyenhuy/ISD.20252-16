package com.hust.soict.ict.aims.subsystems.paypal.service;

import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.order.OrderFinalization;
import com.hust.soict.ict.aims.services.order.PlaceOrderService;
import com.hust.soict.ict.aims.subsystems.PaymentInitiation;
import com.hust.soict.ict.aims.subsystems.PaymentMethod;
import com.hust.soict.ict.aims.subsystems.PaymentService;
import com.hust.soict.ict.aims.subsystems.paypal.PayPalRedirectGateway;
import com.hust.soict.ict.aims.subsystems.Refundable;
import com.hust.soict.ict.aims.subsystems.paypal.PayPalRefundGateway;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalPaymentCapture;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalPaymentInitiation;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalRefundResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class PayPalPaymentServiceImpl extends PaymentService implements PayPalPaymentService, Refundable {

    /**
     * Prefix stamped onto {@code PaymentTransaction.transactionContent} at capture
     * time ({@code "PAYPAL-" + captureId}). Single source of truth so the write
     * path (capture) and the read path (refund) can never drift apart.
     */
    private static final String TRANSACTION_CONTENT_PREFIX = "PAYPAL-";

    private final PayPalRedirectGateway paymentProvider;
    private final PayPalRefundGateway refundGateway;

    public PayPalPaymentServiceImpl(PayPalRedirectGateway paymentProvider,
                                    PayPalRefundGateway refundGateway,
                                    OrderDraftContext orderDraftContext,
                                    OrderFinalization orderFinalization) {
        super(PaymentMethod.PAYPAL, orderDraftContext, orderFinalization);
        this.paymentProvider = paymentProvider;
        this.refundGateway = refundGateway;
    }

    /**
     * Step 1 — create a PayPal payment for the current draft order and return the
     * approval URL the frontend must open.
     */
    @Override
    protected PaymentInitiation startPayment() throws PaymentException {
        Order.Draft order = currentOrder();
        PayPalPaymentInitiation initiation = paymentProvider.createPayment(order.getCheckoutId().toString(), order.getTotalAmount());

        log.info("[PaypalPaymentService] PayPal order {} created for AIMS order {}",
                initiation.providerOrderId(), order.getCheckoutId());

        return initiation;
    }

    /**
     * Step 2 — capture the approved PayPal payment, record the transaction, and
     * finalize the order (persist + email) via {@link PlaceOrderService}.
     *
     * @param providerOrderId the PayPal token returned to the frontend on redirect
     */
    @Override
    public OrderResponse capturePayment(String providerOrderId) throws PaymentException {
        Order.Draft order = currentOrder();

        PayPalPaymentCapture capture = paymentProvider.capturePayment(providerOrderId);

        if (!capture.completed()) {
            throw new PaymentException("PayPal payment was not completed. Status: " + capture.status());
        }

        // Defence in depth: the captured order must reference THIS draft order, so a
        // forged/leaked token cannot be used to finalize someone else's order.
        if (!order.getCheckoutId().toString().equals(capture.referenceId())) {
            throw new PaymentException("Captured PayPal payment does not belong to the current order.");
        }

        log.info("[PaypalPaymentService] PayPal capture {} COMPLETED — finalizing order {}",
                capture.captureId(), order.getCheckoutId());

        // Crucial integration rule: persist the order + send confirmation email.
        // The "PAYPAL-<captureId>" content is later parsed back in refund().
        return finalizePayment(TRANSACTION_CONTENT_PREFIX + capture.captureId());
    }

    /**
     * Step 2' — customer cancelled on PayPal. Best-effort log; the draft is kept
     * intact so the customer can retry or switch back to VietQR.
     */
    @Override
    public void cancelPayment() {
        try {
            Order.Draft order = currentOrder();
            log.info("[PaypalPaymentService] PayPal payment cancelled for draft order {} — draft kept for retry",
                    order.getCheckoutId());
        } catch (OrderNotPlacedException e) {
            log.warn("[PaypalPaymentService] Cancel received but no draft order is present in the session");
        }
    }

    /**
     * Refund a previously captured PayPal payment.
     *
     * <p>Invoked by {@code RefundRegistry} from the order-cancellation /
     * order-rejection event handlers, asynchronously and {@code AFTER_COMMIT}.
     * This path has <b>no draft order in context</b>, so everything needed is read
     * from the persisted {@link PaymentTransaction}:
     * <ul>
     *   <li>the PayPal capture id, parsed out of {@code transactionContent}
     *       ({@code "PAYPAL-<captureId>"});</li>
     *   <li>the amount actually paid (VND), used for the refund body.</li>
     * </ul>
     *
     * <p><b>Contract:</b> {@link Refundable#refund} is {@code void} on
     * purpose — the handlers only need a success/failure signal. We return
     * normally on a {@code COMPLETED} refund (the handler then moves the order to
     * REFUNDED); on any problem we throw {@link PaymentException}, which the
     * handler catches to leave the order in its prior CANCELLED / REJECTED state.
     * The richer {@link PayPalRefundResult} is consumed here for validation rather than
     * surfaced to the domain, keeping the capability interface minimal.
     */
    @Override
    public void refund(PaymentTransaction transaction) throws PaymentException {
        String captureId = extractCaptureId(transaction);
        long vndAmount = resolveRefundAmount(transaction);

        log.info("[PaypalPaymentService] Refunding PayPal capture {} for {} VND (order {})",
                captureId, vndAmount, transaction.getOrder().getId());

        PayPalRefundResult result = refundGateway.refund(captureId, vndAmount);

        if (!result.completed()) {
            throw new PaymentException(
                    "PayPal refund not completed for capture " + captureId + " (status " + result.status() + ")");
        }

        log.info("[PaypalPaymentService] PayPal refund {} COMPLETED for order {}",
                result.refundId(), transaction.getOrder().getId());
    }

    /* ── refund helpers (defensive parsing of OUR own transaction-content convention) ── */

    /**
     * Isolate the raw PayPal {@code capture_id} from the persisted
     * {@code transactionContent}. The {@code "PAYPAL-"} prefix is an AIMS-side
     * convention (chosen in {@link #capturePayment}), so unwrapping it is a domain
     * concern handled here — the subsystem only ever receives a clean capture id.
     */
    private String extractCaptureId(PaymentTransaction transaction) throws PaymentException {
        String content = transaction.getTransactionContent();
        if (content == null || !content.startsWith(TRANSACTION_CONTENT_PREFIX)) {
            throw new PaymentException(
                    "Cannot refund: unexpected PayPal transaction content '" + content + "'");
        }
        String captureId = content.substring(TRANSACTION_CONTENT_PREFIX.length()).trim();
        if (captureId.isEmpty()) {
            throw new PaymentException(
                    "Cannot refund: transaction content carries no PayPal capture id");
        }
        return captureId;
    }

    private long resolveRefundAmount(PaymentTransaction transaction) throws PaymentException {
        Long amount = transaction.getAmountPaid();
        if (amount == null || amount <= 0) {
            throw new PaymentException(
                    "Cannot refund: invalid amount on transaction " + transaction.getId());
        }
        return amount;
    }
}
