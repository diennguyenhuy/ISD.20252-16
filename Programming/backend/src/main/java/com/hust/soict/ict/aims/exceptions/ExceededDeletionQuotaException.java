package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class ExceededDeletionQuotaException extends RuntimeException {
    public ExceededDeletionQuotaException(String message) {
        super(message);
    }
}
