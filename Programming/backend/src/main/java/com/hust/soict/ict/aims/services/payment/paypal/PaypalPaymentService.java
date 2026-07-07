package com.hust.soict.ict.aims.services.payment.paypal;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.dto.response.payment.paypal.PayPalCreateResponse;
import com.hust.soict.ict.aims.exceptions.PaymentException;

public interface PaypalPaymentService {
    OrderResponse capturePayment(String providerOrderId) throws PaymentException;
    void cancelPayment();
}
