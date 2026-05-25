package com.hust.soict.ict.aims.exceptions;

/**
 * Exception thrown when user cancels the payment
 */
public class UserCancelledException extends PaymentException {
    
    public UserCancelledException(String message) {
        super(message);
    }
    
    public UserCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
}

