package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderCancellationException extends RuntimeException {
    public OrderCancellationException(UUID orderId) {
        super("Order " + orderId + " cannot be cancelled.");
    }

    public OrderCancellationException(UUID orderId, Throwable cause) {
        super("Order " + orderId + " cannot be cancelled.", cause);
    }
}
