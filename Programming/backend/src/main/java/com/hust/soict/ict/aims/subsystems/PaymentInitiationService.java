package com.hust.soict.ict.aims.subsystems;

import com.hust.soict.ict.aims.exceptions.UnsupportedPaymentMethodException;
import com.hust.soict.ict.aims.subsystems.exception.PaymentException;

import java.util.Set;

public interface PaymentInitiationService {
    PaymentInitiation initiatePayment(PaymentMethod paymentMethod) throws PaymentException;
    Set<PaymentMethod> getSupportedPaymentMethods();

    default PaymentInitiation initiatePayment(String paymentMethod) throws PaymentException {
        PaymentMethod pm;
        try {
            pm = PaymentMethod.valueOf(paymentMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedPaymentMethodException(paymentMethod, e);
        }
        return initiatePayment(pm);
    }
}
