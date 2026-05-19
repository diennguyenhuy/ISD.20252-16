package com.hust.soict.ict.aims.exceptions;

/**
 * Base exception for payment-related errors
 */
public class PaymentException extends Exception {
    
    public PaymentException(String message) {
        super(message);
    }
    
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}

