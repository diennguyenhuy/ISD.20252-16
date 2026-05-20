package com.hust.soict.ict.aims.subsystems.vietqr;

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
        this.content = sanitizeContent(content);
        this.amount = amount;
        this.orderId = sanitizeOrderId(orderId);
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
    
    /**
     * Sanitize content to meet VietQR requirements:
     * - Max 23 characters
     * - No special characters
     * - Vietnamese without diacritics
     */
    String sanitizeContent(String content) {
        if (content == null) return "";
        
        // Remove diacritics and special characters
        String sanitized = content.replaceAll("[^a-zA-Z0-9 ]", "");
        
        // Truncate to 23 chars
        if (sanitized.length() > 23) {
            sanitized = sanitized.substring(0, 23);
        }
        
        return sanitized;
    }
    
    /**
     * Sanitize order ID to meet VietQR requirements:
     * - Max 13 characters
     * - No special characters
     */
    String sanitizeOrderId(String orderId) {
        if (orderId == null) return "";
        
        // Remove special characters
        String sanitized = orderId.replaceAll("[^a-zA-Z0-9]", "");
        
        // Truncate to 13 chars
        if (sanitized.length() > 13) {
            sanitized = sanitized.substring(0, 13);
        }
        
        return sanitized;
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
