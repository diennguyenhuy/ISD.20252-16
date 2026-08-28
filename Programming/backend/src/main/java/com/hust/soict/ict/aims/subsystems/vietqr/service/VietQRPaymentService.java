package com.hust.soict.ict.aims.subsystems.vietqr.service;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRPaymentStatus;

public interface VietQRPaymentService {
    VietQRPaymentStatus checkPaymentStatus() throws PaymentException;
    Order confirmPayment() throws PaymentException;
}
