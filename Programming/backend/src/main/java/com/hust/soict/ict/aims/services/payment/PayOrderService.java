package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.IPaymentQRCode;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.mapper.PaymentMapper;
import com.hust.soict.ict.aims.models.dto.response.payments.PaymentStatusResponse;
import com.hust.soict.ict.aims.models.dto.response.payments.QRCodeResponse;
import com.hust.soict.ict.aims.subsystems.vietqr.QRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.QRCodePaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;

/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with IPaymentQRCode/VietQRController: DATA
 * + Reason: PayOrderService is the single owner of PaymentTransaction creation
 *           for all payment paths. Its methods:
 *
 *           all work toward the same goal: managing the VietQR payment lifecycle
 *           for an order. No other service creates PaymentTransaction objects.
 *
 *           confirmPayment() reads PaymentCallbackContext to obtain real payment
 *           data supplied by the gateway callback when it is available, and
 *           falls back to local order data otherwise. This keeps PayOrderService
 *           fully decoupled from any specific gateway callback mechanism.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayOrderService {

    private final IPaymentQRCode qrPaymentController;
    private final PaymentMapper paymentMapper;

    public QRCodeResponse generatePaymentQR(Order order) throws PaymentException {
        return paymentMapper.toQRCodeResponse(qrPaymentController.generateQRCode(order));
    }

    public PaymentStatusResponse checkPaymentStatus(Order order) throws PaymentException {
        return paymentMapper.toPaymentStatusResponse(qrPaymentController.checkPaymentStatus(order));
    }

    public PaymentTransaction confirmPayment(Order order) throws PaymentException {
        QRCodePaymentStatus paymentStatus = qrPaymentController.checkPaymentStatus(order);
        if (!paymentStatus.isCompleted()) {
            throw new PaymentException(
                    "Payment verification failed. VietQR status: " + paymentStatus.getStatus());
        }
        log.info("[PayOrderService] VietQR confirmed COMPLETED — creating PaymentTransaction");
        return PaymentTransaction.of(
                "ORD" + order.getId(),
                Instant.now(),
                PaymentTransaction.Method.VIETQR,
                order.getTotalAmount(),
                order
        );
    }
}
