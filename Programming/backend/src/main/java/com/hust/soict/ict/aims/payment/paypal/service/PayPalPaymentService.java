package com.hust.soict.ict.aims.payment.paypal.service;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.payment.exception.PaymentException;

public interface PayPalPaymentService {
    Order capturePayment(String providerOrderId) throws PaymentException;
    void cancelPayment();
}
