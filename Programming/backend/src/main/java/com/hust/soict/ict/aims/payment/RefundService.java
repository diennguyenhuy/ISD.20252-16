package com.hust.soict.ict.aims.payment;

import com.hust.soict.ict.aims.payment.exception.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;

import java.util.Set;

public interface RefundService {
    boolean supports(PaymentMethod paymentMethod);
    void refund(PaymentTransaction paymentTransaction) throws PaymentException;
    Set<PaymentMethod> getSupportedPaymentMethods();

    default boolean supports(String paymentMethod) {
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(paymentMethod.toUpperCase().strip());
        } catch (IllegalArgumentException _) {
            return false;
        }
        return supports(method);
    }
}
