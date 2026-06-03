package com.hust.soict.ict.aims.dto.vietqr.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VietQRTransactionSyncRequest {

    @JsonProperty("transactionid")
    private String transactionId;

    @JsonProperty("transactiontime")
    private Long transactionTime;

    @JsonProperty("referencenumber")
    private String referenceNumber;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("content")
    private String content;

    @JsonProperty("bankaccount")
    private String bankAccount;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("transType")
    private String transType;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("terminalCode")
    private String terminalCode;

    @JsonProperty("subTerminalCode")
    private String subTerminalCode;

    @JsonProperty("serviceCode")
    private String serviceCode;

    @JsonProperty("urlLink")
    private String urlLink;
}
