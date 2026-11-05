package com.hust.soict.ict.aims.payment.vietqr.synccallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VietQRTransactionSyncObject(
        @JsonProperty("reftransactionid")
        String refTransactionId
) {}
