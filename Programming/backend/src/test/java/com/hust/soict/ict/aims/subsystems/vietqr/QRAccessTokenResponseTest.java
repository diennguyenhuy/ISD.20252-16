package com.hust.soict.ict.aims.subsystems.vietqr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QRAccessTokenResponseTest {

    @Test
    @DisplayName("Validate Parsed Token Response")
    void parseValidResponseString() throws Exception {
        String fakeJson = "{\"access_token\":\"abc123\",\"expires_in\":300}";

        ObjectMapper mapper = new ObjectMapper();
        QRAccessTokenResponse response = mapper.readValue(fakeJson, QRAccessTokenResponse.class);

        assertEquals("abc123", response.getAccessToken());
        assertEquals(300, response.getExpiresIn());
    }

}