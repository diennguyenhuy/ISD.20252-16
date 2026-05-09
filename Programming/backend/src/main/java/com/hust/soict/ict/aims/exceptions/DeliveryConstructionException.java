package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

import java.util.Map;

public class DeliveryConstructionException extends RuntimeException {
    private final @Getter Map<String, String> invalidFields;
    public DeliveryConstructionException(Map<String, String> invalidFields) {
        super("Some fields of delivery information are invalid");
        this.invalidFields = invalidFields;
    }
}
