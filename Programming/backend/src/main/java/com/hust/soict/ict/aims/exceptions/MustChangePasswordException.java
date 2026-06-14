package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a user whose {@code must_change_password} flag is set tries to
 * access any endpoint other than the password-change endpoint.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class MustChangePasswordException extends RuntimeException {
    public MustChangePasswordException() {
        super("You must change your password before performing any other action. "
                + "Please use POST /api/profile/password.");
    }
}
