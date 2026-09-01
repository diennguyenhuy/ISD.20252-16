package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderRefundException extends RuntimeException {
    public OrderRefundException(UUID orderId) {
        super("Order " + orderId + " cannot be refunded.");
    }

    public OrderRefundException(UUID orderId, Throwable cause) {
        super("Order " + orderId + " cannot be refunded.", cause);
    }
}
