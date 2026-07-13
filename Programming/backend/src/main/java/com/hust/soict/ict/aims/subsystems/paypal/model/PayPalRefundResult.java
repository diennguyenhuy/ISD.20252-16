package com.hust.soict.ict.aims.subsystems.paypal.model;

import com.hust.soict.ict.aims.subsystems.paypal.PayPalRefundGateway;

/**
 * Provider-agnostic result of {@link PayPalRefundGateway#refund}.
 *
 * <p>Immutable carrier (Java 21 record). Like {@code PaymentInitiation} /
 * {@code PaymentCapture}, it carries <b>no</b> PayPal vocabulary so the AIMS core
 * stays decoupled from the gateway.
 *
 * @param completed {@code true} only when the provider reports a fully settled
 *                  refund (PayPal status {@code COMPLETED}); the order is moved to
 *                  REFUNDED only on this signal
 * @param refundId  provider id of the refund (PayPal refund id), kept for audit/logging
 * @param status    raw provider status (COMPLETED / PENDING / FAILED ...), kept for logging/errors
 */
public record PayPalRefundResult(boolean completed, String refundId, String status) {
}
