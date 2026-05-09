package com.hust.soict.ict.aims.exceptions;

public class DeliveryValidationException extends ValidationException {
    public DeliveryValidationException(String message, String invalidFieldName) {
        super(message, invalidFieldName);
    }
}
