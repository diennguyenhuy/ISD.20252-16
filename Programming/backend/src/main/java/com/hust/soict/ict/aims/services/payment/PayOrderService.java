package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.IPaymentQRCode;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.subsystems.vietqr.QRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.QRCodePaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with IPaymentQRCode/VietQRController: DATA
 * + Reason: PayOrderService has one main responsibility: coordinating the
 *           VietQR payment flow for an order. Its methods generatePaymentQR(),
 *           checkPaymentStatus(), and confirmPayment() all work toward the
 *           same goal of generating a QR code, checking the payment result,
 *           and creating a PaymentTransaction after successful payment.
 *
 *           The coupling with IPaymentQRCode/VietQRController is DATA coupling
 *           because PayOrderService communicates through the IPaymentQRCode
 *           interface by passing an Order object and receiving QRCode or
 *           QRCodePaymentStatus objects. It does not control the internal
 *           logic of the VietQR implementation. Also, because it depends on
 *           the IPaymentQRCode interface instead of a concrete VietQRController,
 *           the coupling is reduced and the design is more flexible.
 */
@Service
@RequiredArgsConstructor
public class PayOrderService {

    private final IPaymentQRCode qrPaymentController;

    public QRCode generatePaymentQR(Order order) throws PaymentException {
        return qrPaymentController.generateQRCode(order);
    }

    public QRCodePaymentStatus checkPaymentStatus(Order order) throws PaymentException {
        return qrPaymentController.checkPaymentStatus(order);
    }

    public PaymentTransaction confirmPayment(Order order) throws PaymentException {
        QRCodePaymentStatus paymentStatus = checkPaymentStatus(order);

        if (!paymentStatus.isCompleted()) {
            throw new PaymentException(
                    "Payment is not completed. Current status: " + paymentStatus.getStatus());
        }

        String content = "VietQR payment for ORDER " + order.getId();
        return PaymentTransaction.of(
                content,
                Instant.now(),
                PaymentTransaction.Method.VIETQR,
                order.getTotalAmount(),
                order
        );
    }
}