package com.hust.soict.ict.aims.subsystems.vietqr;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QRAccessTokenResponseTest {

    @Test
    @DisplayName("Validate Parsed Token Response")
    void parseValidResponseString() {
        String fakeJson = "{\"access_token\":\"abc123\",\"expires_in\":300}";

        QRAccessTokenResponse response = new QRAccessTokenResponse();
        response.parseResponseString(fakeJson);

        assertEquals("abc123", response.getAccessToken());
        assertEquals(300, response.getExpiresIn());
    }

}