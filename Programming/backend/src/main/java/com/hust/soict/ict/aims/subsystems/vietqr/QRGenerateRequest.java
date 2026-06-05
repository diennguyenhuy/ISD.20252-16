package com.hust.soict.ict.aims.subsystems.vietqr;

import lombok.Getter;

/*
 * SOLID Principles: No violations.
 *
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: Only primitives and Strings are passed in; buildRequestString()
 *   returns a plain String. No composite objects or control flags involved.
 */
@Getter
class QRGenerateRequest {
    private String bankCode;
    private String bankAccount;
    private String userBankName;
    private String content;
    private int qrType; // qrType: 0=dynamic, 1=static, 3=semi-dynamic
    private long amount;
    private String orderId;
    private String transType;
    
    QRGenerateRequest(String bankCode, String bankAccount, String userBankName,
                             String content, long amount, String orderId) {
        this.bankCode = bankCode;
        this.bankAccount = bankAccount;
        this.userBankName = userBankName;
        this.content = content;
        this.amount = amount;
        this.orderId = orderId;
        this.qrType = 0;  // Dynamic QR
        this.transType = "C";  // Credit (receiving money)
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
