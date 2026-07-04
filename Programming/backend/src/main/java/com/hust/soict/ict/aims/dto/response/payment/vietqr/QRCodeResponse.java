package com.hust.soict.ict.aims.dto.response.payment.vietqr;

@Deprecated
public record QRCodeResponse(
        String qrCode,
        String qrLink,
        String bankName,
        String bankAccount,
        String userBankName,
        String content,
        Long amount
) {
}
