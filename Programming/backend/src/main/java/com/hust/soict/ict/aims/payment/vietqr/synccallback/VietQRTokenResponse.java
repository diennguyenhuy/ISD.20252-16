package com.hust.soict.ict.aims.payment.vietqr.synccallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VietQRTokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_in")
        int expiresIn
) {}
