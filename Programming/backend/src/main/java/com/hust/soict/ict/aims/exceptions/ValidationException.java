package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

public class ValidationException extends RuntimeException {
    private final @Getter String invalidFieldName;

    public ValidationException(String message, String invalidFieldName) {
        super(message);
        this.invalidFieldName = invalidFieldName;
    }
}
