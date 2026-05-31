package com.hust.soict.ict.aims.subsystems.paypal;

/**
 * Provider-agnostic result of {@link IPaymentProvider#createPayment}.
 *
 * <p>Immutable carrier (Java 21 record). Deliberately contains <b>no</b> PayPal
 * vocabulary so the AIMS core stays decoupled from the gateway.
 *
 * @param providerOrderId opaque id the provider uses to identify this payment
 *                        (PayPal "order id" / token). Needed later for capture.
 * @param approvalUrl     URL the customer must visit to approve the payment.
 */
public record PaymentInitiation(String providerOrderId, String approvalUrl) {
}
