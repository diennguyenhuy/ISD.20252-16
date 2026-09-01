package com.hust.soict.ict.aims.subsystems.exception;

/**
 * Exception thrown when user cancels the payment
 */
public class PaymentCancelledException extends PaymentException {
    
    public PaymentCancelledException(String message) {
        super(message);
    }
    
    public PaymentCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
}

