package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

public class ValidationException extends RuntimeException {
    @Getter
    private String invalidFieldName;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String invalidFieldName) {
        super(message);
        this.invalidFieldName = invalidFieldName;
    }
}
