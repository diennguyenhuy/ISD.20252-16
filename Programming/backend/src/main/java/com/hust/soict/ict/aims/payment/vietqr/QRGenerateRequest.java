package com.hust.soict.ict.aims.payment.vietqr;

record QRGenerateRequest(
        String bankCode,
        String bankAccount,
        String userBankName,
        String content,
        int qrType,
        long amount,
        String orderId,
        String transType
) {
    QRGenerateRequest(String bankCode, String bankAccount, String userBankName, String content, long amount, String orderId) {
        // Dynamic QR
        this(bankCode, bankAccount, userBankName, content, 0, amount, orderId, "C");  // Credit (receiving money)
    }
    
    String buildRequestString() {
        // Build JSON request exactly as VietQR API specification
        // https://api.vietqr.vn/vi/api-vietqr-callback/goi-api-generate-vietqr-code
        return String.format(
            "{\"bankCode\":\"%s\",\"bankAccount\":\"%s\",\"userBankName\":\"%s\"," +
            "\"content\":\"%s\",\"qrType\":%d,\"amount\":%d,\"orderId\":\"%s\",\"transType\":\"%s\"}",
            bankCode, bankAccount, userBankName, 
            content, qrType, amount, orderId, transType
        );
    }
}
