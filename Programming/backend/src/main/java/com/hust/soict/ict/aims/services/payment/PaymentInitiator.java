package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.exceptions.PaymentException;

public interface PaymentInitiator {
    PaymentInitiation initiatePayment(String paymentMethod) throws PaymentException;
}
