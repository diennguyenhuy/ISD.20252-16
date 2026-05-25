package com.hust.soict.ict.aims.subsystems.vietqr;
/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: QRAccessTokenResponse is responsible for handling and interpreting
 *           the access token response returned from the VietQR API. All fields
 *           (accessToken, tokenType, expiresIn) and methods such as
 *           parseResponseString(), isValid(), and getter methods work together
 *           toward the single purpose of storing and validating authentication
 *           token information.
 *
 *           The coupling with VietQRController is DATA coupling because
 *           VietQRController only exchanges authentication-related data through
 *           this object, such as accessToken and expiresIn. The controller
 *           simply retrieves the parsed values and does not depend on or control
 *           the internal parsing implementation of QRAccessTokenResponse.
 */
class QRAccessTokenResponse {
    private String accessToken;
    private String tokenType;
    private int expiresIn;
    
    void parseResponseString(String responseString) {
        if (responseString == null || responseString.isEmpty()) {
            return;
        }
        
        // Extract access_token
        if (responseString.contains("access_token")) {
            int start = responseString.indexOf("\"access_token\":\"") + 16;
            int end = responseString.indexOf("\"", start);
            if (start > 15 && end > start) {
                this.accessToken = responseString.substring(start, end);
            }
        }
        
        // Extract token_type
        if (responseString.contains("token_type")) {
            int start = responseString.indexOf("\"token_type\":\"") + 14;
            int end = responseString.indexOf("\"", start);
            if (start > 13 && end > start) {
                this.tokenType = responseString.substring(start, end);
            }
        }
        
        // Extract expires_in
        if (responseString.contains("expires_in")) {
            int start = responseString.indexOf("\"expires_in\":") + 13;
            int end = start;
            // Find end of number
            while (end < responseString.length() && 
                   (Character.isDigit(responseString.charAt(end)) || responseString.charAt(end) == ' ')) {
                end++;
            }
            if (start < end) {
                try {
                    this.expiresIn = Integer.parseInt(responseString.substring(start, end).trim());
                } catch (NumberFormatException e) {
                    this.expiresIn = 300; // Default 5 minutes
                }
            }
        }
    }
    
    boolean isValid() {
        return accessToken != null && !accessToken.isEmpty();
    }
    
    String getAccessToken() { return accessToken; }  
    String getTokenType() { return tokenType; }
    int getExpiresIn() { return expiresIn; }
}
