package com.hust.soict.ict.aims.exceptions;

import com.hust.soict.ict.aims.services.payment.PaymentMethod;

public class UnsupportedPaymentMethodException extends RuntimeException {

    public UnsupportedPaymentMethodException(PaymentMethod method) {
        super("Unsupported payment method: " + method.name());
    }
}
