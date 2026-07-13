package com.hust.soict.ict.aims.subsystems.paypal;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.exceptions.UserCancelledException;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalPaymentCapture;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalPaymentInitiation;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalRefundResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * FACADE for the PayPal subsystem.
 *
 * <p>Hides four collaborators ({@link PayPalOrdersClient}, {@link PayPalPaymentsClient},
 * {@link PayPalAuthClient} via the clients, and {@link PayPalAmountConverter}) plus
 * all PayPal JSON and link-parsing behind two simple contracts:
 * {@link PayPalRedirectGateway} (pay) and {@link PayPalRefundGateway} (refund).
 *
 * <ul>
 *   <li><b>Cohesion:</b> Functional — orchestrates exactly the PayPal
 *       create / capture / refund lifecycle.</li>
 *   <li><b>Coupling outward:</b> DATA — returns only {@link PayPalPaymentInitiation} /
 *       {@link PayPalPaymentCapture} / {@link PayPalRefundResult}; maps the internal
 *       {@link PayPalApiException} to the AIMS-level {@link PaymentException} so no
 *       PayPal type ever escapes.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
@Slf4j
class PayPalGatewayFacade implements PayPalRedirectGateway, PayPalRefundGateway {

    private final PayPalOrdersClient ordersClient;
    private final PayPalPaymentsClient paymentsClient;
    private final PayPalAmountConverter amountConverter;
    private final PayPalProperties props;

    @Override
    public PayPalPaymentInitiation createPayment(String orderId, long totalAmount) throws PaymentException {

        var amount = new PayPalApiModel.Amount(
                props.getCurrency(),
                amountConverter.toProviderValue(totalAmount));

        // reference_id AND custom_id carry the AIMS order id so we can verify it on capture.
        var unit = new PayPalApiModel.PurchaseUnit(orderId, orderId, amount, null);

        var appCtx = new PayPalApiModel.ApplicationContext(
                props.getReturnUrl(), props.getCancelUrl(),
                "PAY_NOW", "NO_SHIPPING", props.getBrandName());

        var request = new PayPalApiModel.CreateOrderRequest("CAPTURE", List.of(unit), appCtx);

        try {
            PayPalApiModel.OrderResponse resp = ordersClient.createOrder(request);
            String approvalUrl = extractApprovalUrl(resp);
            log.info("[PayPalFacade] Created PayPal order {} (status {}) for AIMS order {}",
                    resp.id(), resp.status(), orderId);
            return new PayPalPaymentInitiation(resp.id(), approvalUrl);
        } catch (PayPalApiException e) {
            log.error("[PayPalFacade] createPayment failed for order {}: {}", orderId, e.getMessage());
            throw new PaymentException("Could not initiate PayPal payment: " + e.getMessage());
        }
    }

    @Override
    public PayPalPaymentCapture capturePayment(String providerOrderId) throws PaymentException {
        try {
            PayPalApiModel.OrderResponse resp = ordersClient.captureOrder(providerOrderId);
            String status = resp.status() == null ? "UNKNOWN" : resp.status();

            // Payer abandoned/voided the approval → domain-specific cancellation signal.
            if ("VOIDED".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)) {
                throw new UserCancelledException("PayPal payment was cancelled before completion.");
            }

            PayPalApiModel.PurchaseUnit unit = firstUnit(resp);
            String referenceId = unit == null ? null
                    : (unit.customId() != null ? unit.customId() : unit.referenceId());
            String captureId = extractCaptureId(unit, resp.id());
            boolean completed = "COMPLETED".equalsIgnoreCase(status);

            log.info("[PayPalFacade] Capture of order {} → status {} (captureId {})",
                    providerOrderId, status, captureId);

            return new PayPalPaymentCapture(completed, captureId, referenceId, status);
        } catch (PayPalApiException e) {
            log.error("[PayPalFacade] capturePayment failed for {}: {}", providerOrderId, e.getMessage());
            throw new PaymentException("Could not capture PayPal payment: " + e.getMessage());
        }
    }

    /**
     * Refund a settled capture. Defensive validation first, then convert the
     * VND amount to the gateway currency, call the Payments API, and translate any
     * subsystem failure into {@link PaymentException}. No PayPal type escapes.
     */
    @Override
    public PayPalRefundResult refund(String captureId, long vndAmount) throws PaymentException {
        if (captureId == null || captureId.isBlank()) {
            throw new PaymentException("Cannot refund: missing PayPal capture id");
        }
        if (vndAmount <= 0) {
            throw new PaymentException("Cannot refund: non-positive amount " + vndAmount);
        }

        // Currency is a gateway-only concern: VND → configured currency (USD), hidden here.
        var amount = new PayPalApiModel.Amount(
                props.getCurrency(),
                amountConverter.toProviderValue(vndAmount));
        var request = new PayPalApiModel.RefundRequest(amount);

        try {
            PayPalApiModel.RefundResponse resp = paymentsClient.refundCapture(captureId, request);
            String status = (resp == null || resp.status() == null) ? "UNKNOWN" : resp.status();
            String refundId = resp == null ? null : resp.id();
            boolean completed = "COMPLETED".equalsIgnoreCase(status);

            log.info("[PayPalFacade] Refund of capture {} → status {} (refundId {})",
                    captureId, status, refundId);

            return new PayPalRefundResult(completed, refundId, status);
        } catch (PayPalApiException e) {
            log.error("[PayPalFacade] refund failed for capture {}: {}", captureId, e.getMessage());
            throw new PaymentException("Could not refund PayPal payment: " + e.getMessage());
        }
    }

    /* ── internal helpers ──────────────────────────────────────────────── */

    private String extractApprovalUrl(PayPalApiModel.OrderResponse resp) throws PaymentException  {
        if (resp == null || resp.links() == null) {
            throw new PaymentException("PayPal returned no links for the created order");
        }
        // application_context flow → rel "approve"; experience_context flow → "payer-action".
        return resp.links().stream()
                .filter(l -> "approve".equalsIgnoreCase(l.rel()) || "payer-action".equalsIgnoreCase(l.rel()))
                .map(PayPalApiModel.Link::href)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new PaymentException("PayPal did not return an approval link"));
    }

    private PayPalApiModel.PurchaseUnit firstUnit(PayPalApiModel.OrderResponse resp) {
        if (resp == null || resp.purchaseUnits() == null || resp.purchaseUnits().isEmpty()) {
            return null;
        }
        return resp.purchaseUnits().get(0);
    }

    private String extractCaptureId(PayPalApiModel.PurchaseUnit unit, String fallback) {
        if (unit != null && unit.payments() != null
                && unit.payments().captures() != null && !unit.payments().captures().isEmpty()) {
            return unit.payments().captures().get(0).id();
        }
        return fallback; // degrade gracefully to the order id
    }
}
