package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class PasswordChangeOverlapException extends RuntimeException {
    public PasswordChangeOverlapException() {
        super("New password must be different from the current password.");
    }
}
