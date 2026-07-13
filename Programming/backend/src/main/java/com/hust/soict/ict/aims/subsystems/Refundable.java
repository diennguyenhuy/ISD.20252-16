package com.hust.soict.ict.aims.subsystems;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;

public interface Refundable {
    PaymentMethod method();
    void refund(PaymentTransaction transaction) throws PaymentException;
}
