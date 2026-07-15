package com.hust.soict.ict.aims.subsystems.vietqr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hust.soict.ict.aims.subsystems.PaymentInitiation;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VietQRCode implements PaymentInitiation {
    @JsonProperty("qrCode")
    private String qrCode;
    @JsonProperty("qrLink")
    private String qrLink;
    @JsonProperty("bankCode")
    private String bankCode;
    @JsonProperty("bankName")
    private String bankName;
    @JsonProperty("bankAccount")
    private String bankAccount;
    @JsonProperty("userBankName")
    private String userBankName;
    @JsonProperty("content")
    private String content;
    @JsonProperty("amount")
    private Long amount; // amount in VND (parsed from VietQR's string field)

    public VietQRCode() {}

    public VietQRCode(String qrCode, String qrLink, String bankCode, String bankName, String bankAccount, String userBankName, String content) {
        this.qrCode = qrCode;
        this.qrLink = qrLink;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.userBankName = userBankName;
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("QRCode{bankName='%s', bankCode='%s', account='%s'}", 
            bankName, bankCode, bankAccount);
    }
    

}
