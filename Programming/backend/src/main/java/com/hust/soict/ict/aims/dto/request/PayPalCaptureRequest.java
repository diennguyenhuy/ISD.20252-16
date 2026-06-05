package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Body of {@code POST /order/payment/paypal/capture}.
 *
 * @param token the PayPal order id PayPal appended to the return URL as
 *              {@code ?token=...} once the customer approved the payment.
 */
public record PayPalCaptureRequest(@NotBlank String token) {
}
