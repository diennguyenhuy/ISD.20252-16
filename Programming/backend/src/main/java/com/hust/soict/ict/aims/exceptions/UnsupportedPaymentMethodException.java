package com.hust.soict.ict.aims.exceptions;

import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;

public class UnsupportedPaymentMethodException extends RuntimeException {

    public UnsupportedPaymentMethodException(PaymentTransaction.Method method) {
        super("Unsupported payment method: " + method.name());
    }
}
