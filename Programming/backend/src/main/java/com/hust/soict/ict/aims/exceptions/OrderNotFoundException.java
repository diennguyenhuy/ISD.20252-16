package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    @Getter
    private final UUID orderId;

    public OrderNotFoundException(UUID orderId) {
        super("Product with id " + orderId + " is not found");
        this.orderId = orderId;
    }
}
