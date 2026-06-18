package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Deprecated
public class ProductConstructionException extends RuntimeException {
    @Getter
    private final Map<String, List<String>> invalidFields;

    public ProductConstructionException(Map<String, List<String>> invalidFields) {
        super("Some fields are invalid when building products");
        this.invalidFields = invalidFields;
    }
}
