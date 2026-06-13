package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a blocked user attempts to perform an operation.
 * Blocked users CAN authenticate (receive a JWT), but their JWT carries
 * an {@code is_blocked} claim that the filter rejects on every subsequent request.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountBlockedException extends RuntimeException {
    public AccountBlockedException() {
        super("Your account has been blocked. Please contact an administrator.");
    }
}
