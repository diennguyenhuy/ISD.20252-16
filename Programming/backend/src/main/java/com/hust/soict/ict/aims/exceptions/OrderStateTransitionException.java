package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderStateTransitionException extends Exception {
    public OrderStateTransitionException(String message) {
        super(message);
    }
}
