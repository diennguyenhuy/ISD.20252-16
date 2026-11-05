package com.hust.soict.ict.aims.payment.vietqr.synccallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VietQRTransactionSyncResponse(
        @JsonProperty("error")
        boolean error,
        @JsonProperty("errorReason")
        String errorReason,
        @JsonProperty("toastMessage")
        String toastMessage,
        @JsonProperty("object")
        VietQRTransactionSyncObject object
) {}
