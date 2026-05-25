package com.hust.soict.ict.aims.subsystems.vietqr;
/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: QRCode is responsible for representing and parsing QR code
 *           information returned from the VietQR subsystem. All attributes
 *           (qrCode, qrLink, bankCode, bankName, bankAccount) and methods
 *           such as parseQRCodeResponse() and extractField() contribute to
 *           the single purpose of storing and processing QR code response data.
 *
 *           The coupling with VietQRController is DATA coupling because
 *           VietQRController only exchanges the QRCode object and related
 *           data values with this class. The controller does not pass
 *           control parameters or manipulate the internal parsing logic
 *           of QRCode. Only the necessary QR response data is shared
 *           between the two components.
 */
public class QRCode {
    private String qrCode; // Base64 or URL of QR code image
    private String qrLink;  // Link to view QR code
    private String bankCode; // Bank code (e.g., "VCB", "TCB")
    private String bankName; // Bank name (e.g., "Vietcombank")
    private String bankAccount; // Bank account number
    
    public QRCode() {}
    
    public QRCode(String qrCode, String qrLink, String bankCode, String bankName, String bankAccount) {
        this.qrCode = qrCode;
        this.qrLink = qrLink;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
    }
    
    public String getQrCode() { return qrCode; }
    
    public String getQrLink() { return qrLink; }
    
    public String getBankCode() { return bankCode; }
    
    public String getBankName() { return bankName; }
    
    public String getBankAccount() { return bankAccount; }
    
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
