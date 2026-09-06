package com.hust.soict.ict.aims.payment;

import com.hust.soict.ict.aims.payment.exception.UnsupportedPaymentMethodException;
import com.hust.soict.ict.aims.payment.exception.PaymentException;

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
