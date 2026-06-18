package com.hust.soict.ict.aims.exceptions;

/**
 * Exception thrown for unknown payment errors
 */
public class UnknownPaymentException extends PaymentException {
    
    public UnknownPaymentException(String message) {
        super(message);
    }
    
    public UnknownPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}

