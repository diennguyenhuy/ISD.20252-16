package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.subsystems.vietqr.IPaymentQRCode;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.mapper.OrderMapper;
import com.hust.soict.ict.aims.models.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.mapper.PaymentMapper;
import com.hust.soict.ict.aims.models.dto.response.payments.PaymentStatusResponse;
import com.hust.soict.ict.aims.models.dto.response.payments.QRCodeResponse;
import com.hust.soict.ict.aims.services.order.OrderFinalization;
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
    private final OrderDraftContext orderDraftContext;

    private final OrderFinalization orderFinalization;

    private final OrderMapper orderMapper;

    private final PaymentMapper paymentMapper;

    public QRCodeResponse generatePaymentQR() throws PaymentException {
        return paymentMapper.toQRCodeResponse(qrPaymentController.generateQRCode(orderDraftContext.getDraftOrder()));
    }

    public PaymentStatusResponse checkPaymentStatus() throws PaymentException {
        return paymentMapper.toPaymentStatusResponse(qrPaymentController.checkPaymentStatus(orderDraftContext.getDraftOrder()));
    }

    public OrderResponse confirmPayment() throws PaymentException {
        Order order = orderDraftContext.getDraftOrder();

        QRCodePaymentStatus paymentStatus = qrPaymentController.checkPaymentStatus(order);
        if (!paymentStatus.isCompleted()) {
            throw new PaymentException(
                    "Payment verification failed. VietQR status: " + paymentStatus.getStatus());
        }
        log.info("[PayOrderService] VietQR confirmed COMPLETED — creating PaymentTransaction");
        return orderFinalization.finalizeOrder(PaymentTransaction.of(
                "ORD" + order.getId(),
                Instant.now(),
                PaymentTransaction.Method.VIETQR,
                order.getTotalAmount(),
                order
        ).getOrder());
    }
}
