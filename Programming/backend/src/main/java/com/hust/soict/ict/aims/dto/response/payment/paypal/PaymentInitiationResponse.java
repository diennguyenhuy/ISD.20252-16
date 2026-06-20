package com.hust.soict.ict.aims.dto.response.payment.paypal;

/**
 * Response DTO for Payment Result
 *
 * Cohesion:
 * - Functional: represents only payment output
 */
public record PaymentInitiationResponse(

        String paymentInstruction,
        String checkoutURL,

        Boolean success,
        String message

) {}