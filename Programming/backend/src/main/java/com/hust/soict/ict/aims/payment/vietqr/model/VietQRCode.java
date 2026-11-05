package com.hust.soict.ict.aims.payment.vietqr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hust.soict.ict.aims.payment.PaymentInitiation;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VietQRCode(
        @JsonProperty("qrCode")
        String qrCode,
        @JsonProperty("qrLink")
        String qrLink,
        @JsonProperty("bankCode")
        String bankCode,
        @JsonProperty("bankName")
        String bankName,
        @JsonProperty("bankAccount")
        String bankAccount,
        @JsonProperty("userBankName")
        String userBankName,
        @JsonProperty("content")
        String content,
        @JsonProperty("amount")
        Long amount // amount in VND (parsed from VietQR's string field)
) implements PaymentInitiation {
    public VietQRCode(String qrCode, String qrLink, String bankCode, String bankName, String bankAccount, String userBankName, String content) {
        this(qrCode, qrLink, bankCode, bankName, bankAccount, userBankName, content, null);
    }

    @Override
    public String toString() {
        return String.format("QRCode{bankName='%s', bankCode='%s', account='%s'}", 
            bankName, bankCode, bankAccount);
    }
}
