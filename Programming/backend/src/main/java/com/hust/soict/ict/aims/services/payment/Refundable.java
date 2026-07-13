package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;

public interface Refundable {
    PaymentMethod method();
    void refund(PaymentTransaction transaction) throws PaymentException;
}
