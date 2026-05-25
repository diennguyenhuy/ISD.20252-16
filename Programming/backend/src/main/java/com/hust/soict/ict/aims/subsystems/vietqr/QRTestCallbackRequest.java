package com.hust.soict.ict.aims.subsystems.vietqr;
/**
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: VietQRController constructs QRTestCallbackRequest by passing individual
 *           primitive/String arguments (bankAccount, content, amount, bankCode). It then
 *           calls buildRequestString() which returns a plain String. No composite object
 *           or control flag is passed. The class has one purpose, and every element contributes
 *           directly to that purpose.
 */
class QRTestCallbackRequest {
    private final String bankAccount;
    private final String content;
    private final long amount;
    private final String bankCode;
    private final String transType;
    QRTestCallbackRequest(String bankAccount, String content, long amount,
                          String bankCode) {
        this.bankAccount = bankAccount;
        this.content = content;
        this.amount = amount;
        this.bankCode = bankCode;
        this.transType = "C";
    }
    /**
     * Builds the JSON request string for the VietQR test callback API.
     *
     * @return JSON string matching the VietQR test callback API specification
     */
    String buildRequestString() {
        return String.format(
                "{\"bankAccount\":\"%s\",\"content\":\"%s\",\"amount\":%d," +
                        "\"bankCode\":\"%s\",\"transType\":\"%s\"}",
                bankAccount, content, amount, bankCode, transType
        );
    }
}