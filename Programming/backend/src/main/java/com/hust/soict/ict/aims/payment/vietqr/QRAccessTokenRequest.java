package com.hust.soict.ict.aims.payment.vietqr;

import java.util.Base64;

record QRAccessTokenRequest(
        String username,
        String password
) {
    String buildAuthorizationHeader() {
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }
}
