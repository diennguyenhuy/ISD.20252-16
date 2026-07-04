package com.hust.soict.ict.aims.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedPaymentMethodException extends RuntimeException {

    public UnsupportedPaymentMethodException(String method) {
        super("Payment method is not supported or unknown: " + method);
    }

    public UnsupportedPaymentMethodException(String method, Throwable cause) {
        super("Payment method is not supported or unknown: " + method, cause);
    }
}
