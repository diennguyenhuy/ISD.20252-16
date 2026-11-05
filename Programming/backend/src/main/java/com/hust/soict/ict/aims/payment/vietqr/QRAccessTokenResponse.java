package com.hust.soict.ict.aims.payment.vietqr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
record QRAccessTokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_in")
        int expiresIn
) {
    boolean isValid() {
        return accessToken != null && !accessToken.isEmpty();
    }
}
