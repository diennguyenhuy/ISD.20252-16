package com.hust.soict.ict.aims.subsystems.vietqr;


import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;

public interface IPaymentQRCode {
    QRCode generateQRCode(Order order) throws PaymentException;
    QRCodePaymentStatus checkPaymentStatus(Order order) throws PaymentException;
}
