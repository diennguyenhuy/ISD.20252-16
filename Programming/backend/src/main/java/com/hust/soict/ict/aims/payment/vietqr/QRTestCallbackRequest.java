package com.hust.soict.ict.aims.payment.vietqr;

record QRTestCallbackRequest(
        String bankAccount,
        String content,
        long amount,
        String bankCode,
        String transType
) {
    QRTestCallbackRequest(String bankAccount, String content, long amount, String bankCode) {
        this(bankAccount, content, amount, bankCode, "C");
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