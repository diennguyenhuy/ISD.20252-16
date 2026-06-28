package com.hust.soict.ict.aims.subsystems.vietqr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: VietQRController only reads status and message via getters or
 *   isCompleted(); no control flags or internal parsing logic is shared.
 */
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QRCodePaymentStatus {
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")
    private String message;
    
    public QRCodePaymentStatus() {}
    
    public QRCodePaymentStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Helper methods
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status);
    }

    public boolean isFailed() {
        return "FAILED".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return String.format("PaymentStatus{status='%s', message='%s'}", status, message);
    }
}

