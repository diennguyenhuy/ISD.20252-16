package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class PasswordChangeConfirmationException extends RuntimeException {
    public PasswordChangeConfirmationException() {
        super("New password and confirmation password do not match.");
    }
}
