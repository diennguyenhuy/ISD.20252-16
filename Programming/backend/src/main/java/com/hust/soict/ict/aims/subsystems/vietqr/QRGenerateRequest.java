package com.hust.soict.ict.aims.subsystems.vietqr;
/**
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: VietQRController constructs QRGenerateRequest by passing individual primitive
 *           and String arguments (bankCode, accountNo, accountName, content, amount,
 *           orderId). It then calls buildRequestString() which returns a plain String.
 *           Only the exact data needed for the QR generation request is exchanged;
 *           no composite objects or control flags are involved. Moreover, All fields and methods
 *           work together toward a single well-defined goal.
 */
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
    

    String getBankCode() { return bankCode; }
    String getBankAccount() { return bankAccount; }
    String getUserBankName() { return userBankName; }
    String getContent() { return content; }
    int getQrType() { return qrType; }
    long getAmount() { return amount; }
    String getOrderId() { return orderId; }
    String getTransType() { return transType; }
}
