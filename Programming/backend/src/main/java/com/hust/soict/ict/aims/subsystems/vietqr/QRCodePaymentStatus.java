package com.hust.soict.ict.aims.subsystems.vietqr;
/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: QRCodePaymentStatus has one clear responsibility: representing and
 *           interpreting the payment status returned from VietQR. Its fields
 *           status and message, together with methods such as isCompleted(),
 *           isPending(), isFailed(), isCancelled(), and parseResponseString(),
 *           all support the same purpose of checking payment result status.
 *
 *           The coupling with VietQRController is DATA coupling because
 *           VietQRController only needs to receive or use simple status data,
 *           such as status and message, through this object or its getter methods.
 *           No control flags are passed, and QRCodePaymentStatus does not control
 *           the internal logic of VietQRController.
 */
public class QRCodePaymentStatus {
    private String status;    // "PENDING", "COMPLETED", "FAILED", "CANCELLED"
    private String message;   // Additional message about payment status
    
    public QRCodePaymentStatus() {}
    
    public QRCodePaymentStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    // Helper methods
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }
    
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }
    
    public boolean isFailed() {
        return "FAILED".equalsIgnoreCase(status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }
    
    public void parseResponseString(String response) {
        if (response == null || response.isEmpty()) {
            this.status = "UNKNOWN";
            this.message = "Empty response";
            return;
        }
        
        // Parse "status" field: "SUCCESS" or "FAILED"
        if (response.contains("\"status\"")) {
            int start = response.indexOf("\"status\":\"") + 10;
            int end = response.indexOf("\"", start);
            if (start > 9 && end > start) {
                String parsedStatus = response.substring(start, end).toUpperCase();
                if ("SUCCESS".equals(parsedStatus)) {
                    this.status = "COMPLETED";
                } else {
                    this.status = parsedStatus;
                }
            }
        }
        
        // Parse "message" field
        if (response.contains("\"message\"")) {
            int start = response.indexOf("\"message\":\"") + 11;
            int end = response.indexOf("\"", start);
            if (start > 10 && end > start) {
                this.message = response.substring(start, end);
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("PaymentStatus{status='%s', message='%s'}", status, message);
    }
}

