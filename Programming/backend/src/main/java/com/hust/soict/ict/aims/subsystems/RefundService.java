package com.hust.soict.ict.aims.subsystems;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;

import java.util.Set;

public interface RefundService {
    boolean supports(PaymentMethod paymentMethod);
    void refund(PaymentTransaction paymentTransaction) throws PaymentException;
    Set<PaymentMethod> getSupportedPaymentMethods();

    default boolean supports(String paymentMethod) {
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return supports(method);
    }
}
