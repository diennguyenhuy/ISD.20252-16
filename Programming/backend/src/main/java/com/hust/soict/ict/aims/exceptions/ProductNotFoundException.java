package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    @Getter
    private final UUID productId;

    public ProductNotFoundException(UUID productId) {
        super("Product with id " + productId + " is not found");
        this.productId = productId;
    }
}
