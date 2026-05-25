package com.hust.soict.ict.aims.subsystems.vietqr;

import java.util.Base64;
/**
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: VietQRController instantiates QRAccessTokenRequest by passing two primitive
 *           String values (username, password) and only calls buildAuthorizationHeader()
 *           which returns a plain String. Only the minimal required data is exchanged;
 *           no composite objects or control flags pass between the two classes. The class has exactly
 *           one clear responsibility.
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

