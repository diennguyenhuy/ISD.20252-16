package com.hust.soict.ict.aims.subsystems.paypal;

/**
 * Provider-agnostic result of {@link IPaymentProvider#capturePayment}.
 *
 * @param completed   {@code true} only when the provider reports a fully settled payment
 * @param captureId   provider id of the settled capture (used as our transaction content)
 * @param referenceId the merchant reference we attached at creation time — for PayPal this
 *                    is the AIMS order id, used to defend against capturing a token that
 *                    belongs to a different order
 * @param status      raw provider status (COMPLETED / PENDING / VOIDED ...), kept for logging/errors
 */
public record PaymentCapture(boolean completed, String captureId, String referenceId, String status) {
}
