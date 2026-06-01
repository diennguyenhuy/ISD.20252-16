package com.hust.soict.ict.aims.subsystems.vietqr;
/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: VietQRController only reads QR data fields via getters after
 *   calling parseQRCodeResponse(). No control flags or internal logic shared.
 */
public class QRCode {
    private String qrCode;
    private String qrLink;
    private String bankCode;
    private String bankName;
    private String bankAccount;
    private String userBankName;
    private String content;
    private Long amount; // amount in VND (parsed from VietQR's string field)

    public QRCode() {}

    public QRCode(String qrCode, String qrLink, String bankCode, String bankName, String bankAccount, String userBankName, String content) {
        this.qrCode = qrCode;
        this.qrLink = qrLink;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.userBankName = userBankName;
        this.content = content;
    }
    
    public String getQrCode() { return qrCode; }
    public String getQrLink() { return qrLink; }
    public String getBankCode() { return bankCode; }
    public String getBankName() { return bankName; }
    public String getBankAccount() { return bankAccount; }
    public String getUserBankName() { return userBankName; }
    public String getContent() { return content; }
    public Long getAmount() { return amount; }

    @Override
    public String toString() {
        return String.format("QRCode{bankName='%s', bankCode='%s', account='%s'}", 
            bankName, bankCode, bankAccount);
    }
    
    public void parseQRCodeResponse(String response) {
        if (response == null || response.isBlank()) {
            return;
        }

        this.qrCode = extractField(response, "qrCode");
        this.qrLink = extractField(response, "qrLink");
        this.bankCode = extractField(response, "bankCode");
        this.bankName = extractField(response, "bankName");
        this.bankAccount = extractField(response, "bankAccount");
        this.userBankName = extractField(response, "userBankName");
        this.content = extractField(response, "content");
        // VietQR returns amount as a string e.g. "1455700"
        String amountStr = extractField(response, "amount");
        if (amountStr != null && !amountStr.isBlank()) {
            try { this.amount = Long.parseLong(amountStr); } catch (NumberFormatException ignored) {}
        }
    }

    /**
     * Extract string field value from simple JSON response.
     */
    private static String extractField(String json, String fieldName) {

        String key = "\"" + fieldName + "\"";

        int keyIndex = json.indexOf(key);
        if (keyIndex < 0) return null;

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex < 0) return null;

        int valueStart = json.indexOf("\"", colonIndex);
        if (valueStart < 0) return null;

        valueStart++; // skip opening quote

        int valueEnd = json.indexOf("\"", valueStart);
        if (valueEnd < 0) return null;

        return json.substring(valueStart, valueEnd);
    }
}
