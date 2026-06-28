package com.hust.soict.ict.aims.subsystems.vietqr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: VietQRController only reads accessToken and expiresIn via getters;
 *   no control flags or internal parsing logic is shared.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class QRAccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("expires_in")
    private int expiresIn;
    

    boolean isValid() {
        return accessToken != null && !accessToken.isEmpty();
    }
}
