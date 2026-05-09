package com.hust.soict.ict.aims.models.dto.response.order;

//TODO: Refine
public record PaymentInitiationResponse(
        //QR Code String if the method is VietQR, null otherwise
        String paymentInstruction,
        //Check out link if the method is PayPal, null otherwise
        String checkoutURL
) {
}
