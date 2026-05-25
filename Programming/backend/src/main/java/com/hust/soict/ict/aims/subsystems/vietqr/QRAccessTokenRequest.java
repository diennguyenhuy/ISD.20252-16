package com.hust.soict.ict.aims.subsystems.vietqr;

import java.util.Base64;

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

