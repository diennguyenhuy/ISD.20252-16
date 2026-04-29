package com.hust.soict.ict.aims.exceptions;

public class ProductValidationException extends ValidationException {

    public ProductValidationException(String message) {
        super(message);
    }

    public ProductValidationException(String message, String invalidFieldName) {
        super(message, invalidFieldName);
    }

}
