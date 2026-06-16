package com.hust.soict.ict.aims.services.payment.contract;

import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.payment.PaymentMethod;

public interface IRefundCapability {
    PaymentMethod method();
    void refund(PaymentTransaction transaction);
}
