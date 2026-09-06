package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class PasswordNotCorrectException extends RuntimeException {
    public PasswordNotCorrectException() {
        super("Current password is incorrect");
    }
}
