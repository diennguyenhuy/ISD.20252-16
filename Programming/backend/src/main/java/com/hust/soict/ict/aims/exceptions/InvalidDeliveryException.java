package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

import java.util.Map;

@Deprecated
public class InvalidDeliveryException extends RuntimeException {
    private final @Getter Map<String, String> invalidFields;
    public InvalidDeliveryException(Map<String, String> invalidFields) {
        super("Some fields of delivery information are invalid");
        this.invalidFields = invalidFields;
    }
}
