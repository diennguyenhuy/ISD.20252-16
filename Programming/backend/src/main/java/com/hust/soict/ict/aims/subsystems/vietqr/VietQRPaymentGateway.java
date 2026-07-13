package com.hust.soict.ict.aims.subsystems.vietqr;


import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRPaymentStatus;

public interface VietQRPaymentGateway {
    VietQRCode generateQRCode(String orderId, long totalAmount) throws PaymentException;
    VietQRPaymentStatus checkPaymentStatus(String orderId, long totalAmount) throws PaymentException;
}
