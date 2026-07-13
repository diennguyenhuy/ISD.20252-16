package com.hust.soict.ict.aims.subsystems.paypal.service;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.exceptions.PaymentException;

public interface PayPalPaymentService {
    OrderResponse capturePayment(String providerOrderId) throws PaymentException;
    void cancelPayment();
}
