package com.hust.soict.ict.aims.subsystems;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;

import java.util.Set;

public interface PaymentInitiationService {
    PaymentInitiation initiatePayment(String paymentMethod) throws PaymentException;
    Set<PaymentMethod> getSupportedPaymentMethods();
}
