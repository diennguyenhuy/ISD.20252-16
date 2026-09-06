package com.hust.soict.ict.aims.payment.vietqr.service;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.payment.exception.PaymentException;
import com.hust.soict.ict.aims.payment.vietqr.model.VietQRPaymentStatus;

public interface VietQRPaymentService {
    VietQRPaymentStatus checkPaymentStatus() throws PaymentException;
    Order confirmPayment() throws PaymentException;
}
