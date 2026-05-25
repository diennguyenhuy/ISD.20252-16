package com.hust.soict.ict.aims;


import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.subsystems.vietqr.QRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.QRCodePaymentStatus;

public interface IPaymentQRCode {
    QRCode generateQRCode(Order order) throws PaymentException;
    QRCodePaymentStatus checkPaymentStatus(Order order) throws PaymentException;
}
