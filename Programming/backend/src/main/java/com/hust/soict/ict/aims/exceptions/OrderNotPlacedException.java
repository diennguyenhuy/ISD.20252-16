package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderNotPlacedException extends RuntimeException {
    public OrderNotPlacedException(String message) {
        super(message);
    }
}
