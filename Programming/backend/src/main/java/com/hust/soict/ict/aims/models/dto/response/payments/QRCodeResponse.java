package com.hust.soict.ict.aims.models.dto.response.payments;

public record QRCodeResponse(
        String qrCode,
        String qrLink,
        String bankName,
        String bankAccount
) {
}
