package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(UUID orderId) {
        super("Order with id " + orderId + " is not found");
    }

    public OrderNotFoundException(UUID orderId, String status) {
        super("Order with id " + orderId + " and status " + status + " is not found");
    }
}
