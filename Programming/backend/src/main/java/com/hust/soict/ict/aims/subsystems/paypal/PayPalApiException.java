package com.hust.soict.ict.aims.subsystems.paypal;

/**
 * Internal subsystem exception.
 *
 * <p>Package-private on purpose: it never escapes the {@code subsystems.paypal}
 * package. {@link PayPalGatewayFacade} catches it and re-throws the AIMS-level
 * {@code PaymentException}, so the core never has to import a PayPal type.
 */
class PayPalApiException extends RuntimeException {

    PayPalApiException(String message) {
        super(message);
    }

    PayPalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
