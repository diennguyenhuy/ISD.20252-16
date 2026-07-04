package com.hust.soict.ict.aims.services.payment.vietqr;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.dto.response.payment.vietqr.QRCodeResponse;
import com.hust.soict.ict.aims.dto.response.payment.vietqr.QRPaymentStatusResponse;
import com.hust.soict.ict.aims.exceptions.PaymentException;

public interface VietQRPaymentService {
    @Deprecated
    QRCodeResponse generatePaymentQR() throws PaymentException;
    QRPaymentStatusResponse checkPaymentStatus() throws PaymentException;
    OrderResponse confirmPayment() throws PaymentException;
}
