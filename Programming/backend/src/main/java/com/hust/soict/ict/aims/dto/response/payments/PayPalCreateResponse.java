package com.hust.soict.ict.aims.dto.response.payments;

/**
 * Response of {@code POST /order/payment/paypal/create}.
 *
 * @param approvalUrl     URL the frontend opens (new tab / redirect) so the
 *                        customer can approve the payment on PayPal
 * @param providerOrderId PayPal order id (a.k.a. "token"). PayPal also appends
 *                        this to the return URL, so the frontend usually reads it
 *                        from the redirect — it is returned here for completeness.
 */
public record PayPalCreateResponse(String approvalUrl, String providerOrderId) {
}
