package com.hust.soict.ict.aims.subsystems.vietqr;
/*
 * SOLID Principles: No violations.
 *
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: Only primitives and Strings are passed in; buildRequestString()
 *   returns a plain String. No composite objects or control flags involved.
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