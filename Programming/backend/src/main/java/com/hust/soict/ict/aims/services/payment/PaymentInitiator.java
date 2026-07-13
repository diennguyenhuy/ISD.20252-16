package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.exceptions.PaymentException;

import java.util.Set;

public interface PaymentInitiator {
    PaymentInitiation initiatePayment(String paymentMethod) throws PaymentException;
    Set<PaymentMethod> getSupportedPaymentMethods();
}
