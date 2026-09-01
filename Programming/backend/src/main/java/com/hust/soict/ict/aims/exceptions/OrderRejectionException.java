package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderRejectionException extends RuntimeException {
    public OrderRejectionException(UUID orderId) {
        super("Order " + orderId + " cannot be rejected.");
    }

    public OrderRejectionException(UUID orderId, Throwable cause) {
        super("Order " + orderId + " cannot be rejected.", cause);
    }
}
