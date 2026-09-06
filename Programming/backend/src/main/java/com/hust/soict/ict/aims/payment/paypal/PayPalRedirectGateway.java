package com.hust.soict.ict.aims.payment.paypal;

import com.hust.soict.ict.aims.payment.exception.PaymentException;
import com.hust.soict.ict.aims.payment.paypal.model.PayPalPaymentCapture;
import com.hust.soict.ict.aims.payment.paypal.model.PayPalPaymentInitiation;

public interface PayPalRedirectGateway {
    PayPalPaymentInitiation createPayment(String orderId, long totalAmount) throws PaymentException;
    PayPalPaymentCapture capturePayment(String providerOrderId) throws PaymentException;
}
