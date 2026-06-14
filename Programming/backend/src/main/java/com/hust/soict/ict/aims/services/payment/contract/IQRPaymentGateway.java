package com.hust.soict.ict.aims.services.payment.contract;


import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.subsystems.vietqr.model.QRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.model.QRCodePaymentStatus;

public interface IQRPaymentGateway {
    QRCode generateQRCode(String orderId, long totalAmount) throws PaymentException;
    QRCodePaymentStatus checkPaymentStatus(String orderId, long totalAmount) throws PaymentException;
}
