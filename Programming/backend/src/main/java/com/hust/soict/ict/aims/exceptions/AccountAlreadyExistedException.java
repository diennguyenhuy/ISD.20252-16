package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountAlreadyExistedException extends RuntimeException {
    public AccountAlreadyExistedException(String message) {
        super(message);
    }
}
