package com.hust.soict.ict.aims.subsystems.paypal;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalRefundResult;

/**
 * Provider-agnostic <b>refund port</b> for redirect-style gateways.
 *
 * <p>Kept separate from {@link PayPalRedirectGateway} on purpose
 * (Interface Segregation): paying and refunding are distinct capabilities, and a
 * future provider may support one without the other. The single subsystem
 * {@code Facade} happens to implement both, but the core depends on each
 * narrowly.
 *
 * <p><b>Boundary rule:</b> the only thing that crosses this contract is a clean
 * {@code captureId} string and an amount expressed in <b>VND</b> (the currency
 * AIMS actually thinks in). Currency conversion, PayPal JSON, HTTP, and every
 * PayPal type stay hidden inside the subsystem. Implementations must translate
 * their internal failures into {@link PaymentException} — no provider exception
 * may leak.
 *
 * <p>Mirrors the established convention of {@link PayPalRedirectGateway},
 * which already returns the subsystem's {@code model} records
 * ({@code PaymentInitiation} / {@code PaymentCapture}); this port returns
 * {@link PayPalRefundResult} in the same spirit.
 */
public interface PayPalRefundGateway {

    /**
     * Refund a previously captured payment.
     *
     * @param captureId the raw provider capture id (already stripped of any
     *                  AIMS-side transaction-content prefix by the caller)
     * @param vndAmount the amount to refund, in VND; the subsystem converts it to
     *                  the gateway currency before calling the provider
     * @return a provider-agnostic {@link PayPalRefundResult} describing the outcome
     * @throws PaymentException if the refund could not be requested/processed
     */
    PayPalRefundResult refund(String captureId, long vndAmount) throws PaymentException;
}
