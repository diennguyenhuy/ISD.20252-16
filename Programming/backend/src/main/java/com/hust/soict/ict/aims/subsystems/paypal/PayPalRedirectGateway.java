package com.hust.soict.ict.aims.subsystems.paypal;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalPaymentCapture;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalPaymentInitiation;

public interface PayPalRedirectGateway {
    PayPalPaymentInitiation createPayment(String orderId, long totalAmount) throws PaymentException;
    PayPalPaymentCapture capturePayment(String providerOrderId) throws PaymentException;
}
