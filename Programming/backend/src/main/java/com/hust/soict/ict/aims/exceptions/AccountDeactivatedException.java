package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a deactivated user attempts to authenticate.
 * Spring Security surfaces this via DisabledException; we also throw it
 * explicitly in AuthService for a clear, user-facing message.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountDeactivatedException extends RuntimeException {
    public AccountDeactivatedException() {
        super("This account has been deactivated. Please contact an administrator.");
    }
}
