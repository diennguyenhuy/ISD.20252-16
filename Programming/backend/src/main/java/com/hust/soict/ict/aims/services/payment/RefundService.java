package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;

import java.util.Set;

public interface RefundService {
    boolean supports(String paymentMethod);
    void refund(PaymentTransaction paymentTransaction) throws PaymentException;
    Set<PaymentMethod> getSupportedPaymentMethods();
}
