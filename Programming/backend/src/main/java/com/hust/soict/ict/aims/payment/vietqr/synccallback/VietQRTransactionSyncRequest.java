package com.hust.soict.ict.aims.payment.vietqr.synccallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VietQRTransactionSyncRequest(
        @JsonProperty("transactionid")
        String transactionId,
        @JsonProperty("transactiontime")
        Long transactionTime,
        @JsonProperty("referencenumber")
        String referenceNumber,
        @JsonProperty("amount")
        Long amount,
        @JsonProperty("content")
        String content,
        @JsonProperty("bankaccount")
        String bankAccount,
        @JsonProperty("orderId")
        String orderId,
        @JsonProperty("transType")
        String transType,
        @JsonProperty("sign")
        String sign,
        @JsonProperty("terminalCode")
        String terminalCode,
        @JsonProperty("subTerminalCode")
        String subTerminalCode,
        @JsonProperty("serviceCode")
        String serviceCode,
        @JsonProperty("urlLink")
        String urlLink
) {}
