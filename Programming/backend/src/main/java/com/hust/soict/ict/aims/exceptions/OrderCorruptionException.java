package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class OrderCorruptionException extends RuntimeException {
    public OrderCorruptionException(String message) {
        super(message);
    }
}
