package com.hust.soict.ict.aims.subsystems.vietqr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VietQRPaymentStatus {
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")
    private String message;
    
    public VietQRPaymentStatus() {}
    
    public VietQRPaymentStatus(String status, String message) {
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

