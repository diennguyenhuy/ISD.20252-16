package com.hust.soict.ict.aims.payment.vietqr;


import com.hust.soict.ict.aims.payment.exception.PaymentException;
import com.hust.soict.ict.aims.payment.vietqr.model.VietQRCode;
import com.hust.soict.ict.aims.payment.vietqr.model.VietQRPaymentStatus;

public interface VietQRPaymentGateway {
    VietQRCode generateQRCode(String orderId, long totalAmount) throws PaymentException;
    VietQRPaymentStatus checkPaymentStatus(String orderId, long totalAmount) throws PaymentException;
}
