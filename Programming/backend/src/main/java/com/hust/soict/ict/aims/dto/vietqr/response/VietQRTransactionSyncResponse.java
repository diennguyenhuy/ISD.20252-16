package com.hust.soict.ict.aims.dto.vietqr.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VietQRTransactionSyncResponse {

    @JsonProperty("error")
    private boolean error;

    @JsonProperty("errorReason")
    private String errorReason;

    @JsonProperty("toastMessage")
    private String toastMessage;

    @JsonProperty("object")
    private VietQRTransactionSyncObject object;
}
