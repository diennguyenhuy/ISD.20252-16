package com.hust.soict.ict.aims.subsystems.exception;

import com.hust.soict.ict.aims.subsystems.PaymentMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedPaymentMethodException extends RuntimeException {

    public UnsupportedPaymentMethodException(PaymentMethod method) {
        super("Payment method is not supported: " + method);
    }

    public UnsupportedPaymentMethodException(PaymentMethod method, Throwable cause) {
        super("Payment method is not supported: " + method, cause);
    }

    public UnsupportedPaymentMethodException(String method) {
        super("Payment method is not supported or unknown: " + method);
    }

    public UnsupportedPaymentMethodException(String method, Throwable cause) {
        super("Payment method is not supported or unknown: " + method, cause);
    }
}
