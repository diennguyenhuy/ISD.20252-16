package com.hust.soict.ict.aims.payment.vietqr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VietQRPaymentStatus(
        @JsonProperty("status")
        String status,
        @JsonProperty("message")
        String message
) {
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
