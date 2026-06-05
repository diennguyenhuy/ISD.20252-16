package com.hust.soict.ict.aims.subsystems.vietqr;


import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;

public interface IPaymentQRCode {
    QRCode generateQRCode(String orderId, long totalAmount) throws PaymentException;
    QRCodePaymentStatus checkPaymentStatus(String orderId, long totalAmount) throws PaymentException;
}
