package com.hust.soict.ict.aims.models.dto.response.order;

public record PaymentInitiationResponse(

        // VietQR
        String paymentInstruction,

        // PayPal
        String checkoutURL,

        // Credit Card
        Boolean success,
        String message

) {
}