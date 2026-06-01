package com.hust.soict.ict.aims.subsystems.vietqr;

import java.util.Base64;
/*
 * SOLID Principles: No violations.
 *
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: Only two primitive Strings (username, password) are passed in;
 *   buildAuthorizationHeader() returns a plain String.
 */
class QRAccessTokenRequest {
    private String username;
    private String password;
    
    QRAccessTokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    
    String buildAuthorizationHeader() {
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }
    
    String getUsername() { return username; }
    String getPassword() { return password; }
}

