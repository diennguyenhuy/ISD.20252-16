package com.hust.soict.ict.aims.subsystems;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;

import java.util.Set;

public interface PaymentInitiator {
    PaymentInitiation initiatePayment(String paymentMethod) throws PaymentException;
    Set<PaymentMethod> getSupportedPaymentMethods();
}
