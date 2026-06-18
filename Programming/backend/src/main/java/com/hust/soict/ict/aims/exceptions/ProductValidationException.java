package com.hust.soict.ict.aims.exceptions;

@Deprecated
public class ProductValidationException extends ValidationException {
    public ProductValidationException(String message, String invalidFieldName) {
        super(message, invalidFieldName);
    }
}
