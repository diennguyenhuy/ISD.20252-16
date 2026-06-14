package com.hust.soict.ict.aims.dto.response.payment.vietqr.synccallback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VietQRTransactionSyncObject {
    @JsonProperty("reftransactionid")
    private String refTransactionId;
}
