package com.hust.soict.ict.aims.subsystems.vietqr;

import lombok.Getter;
import lombok.Setter;

/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: VietQRController only reads status and message via getters or
 *   isCompleted(); no control flags or internal parsing logic is shared.
 */
@Getter @Setter
public class QRCodePaymentStatus {
    private String status;    // "PENDING", "COMPLETED", "FAILED", "CANCELLED"
    private String message;   // Additional message about payment status
    
    public QRCodePaymentStatus() {}
    
    public QRCodePaymentStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }

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

