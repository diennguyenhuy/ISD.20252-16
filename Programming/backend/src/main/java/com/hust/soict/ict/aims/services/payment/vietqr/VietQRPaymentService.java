package com.hust.soict.ict.aims.services.payment.vietqr;

import com.hust.soict.ict.aims.dto.response.payment.vietqr.QRPaymentStatusResponse;
import com.hust.soict.ict.aims.services.payment.PaymentMethod;
import com.hust.soict.ict.aims.services.payment.PaymentService;
import com.hust.soict.ict.aims.services.payment.contract.IQRPaymentGateway;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.dto.response.payment.vietqr.QRCodeResponse;
import com.hust.soict.ict.aims.services.order.OrderFinalization;
import com.hust.soict.ict.aims.subsystems.vietqr.model.QRCodePaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
/*
 * SOLID Principles
 *
 * SRP: confirmPayment() mixes payment verification with order finalization,
 *   which belongs to a different domain.
 *   Improvement: delegate orderFinalization to a CheckoutOrchestrator.
 *
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with IPaymentQRCode/VietQRController: DATA
 * + Reason: PayOrderService is the single owner of the VietQR payment lifecycle.
 *           It delegates to IPaymentQRCode for gateway calls and to
 *           OrderFinalization for order persistence, keeping both concerns loosely coupled.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VietQRPaymentService implements PaymentService {

    private final IQRPaymentGateway qrPaymentGateway;
    private final OrderDraftContext orderDraftContext;

    private final OrderFinalization orderFinalization;

    @Override
    public PaymentMethod method() {
        return PaymentMethod.VIETQR;
    }

    public QRCodeResponse generatePaymentQR() throws PaymentException {
        var code = qrPaymentGateway.generateQRCode(orderDraftContext.getDraftOrder().getId().toString(), orderDraftContext.getDraftOrder().getTotalAmount());

        return new QRCodeResponse(code.getQrCode(), code.getQrLink(), code.getBankName(), code.getBankAccount(), code.getUserBankName(), code.getContent(), code.getAmount());
    }

    public QRPaymentStatusResponse checkPaymentStatus() throws PaymentException {
        var status = qrPaymentGateway.checkPaymentStatus(orderDraftContext.getDraftOrder().getId().toString(), orderDraftContext.getDraftOrder().getTotalAmount());

        return new QRPaymentStatusResponse(status.getStatus(), status.getMessage());
    }

    public OrderResponse confirmPayment() throws PaymentException {
        Order order = orderDraftContext.getDraftOrder();

        QRCodePaymentStatus paymentStatus = qrPaymentGateway.checkPaymentStatus(order.getId().toString(), order.getTotalAmount());
        if (!paymentStatus.isCompleted()) {
            throw new PaymentException("Payment verification failed. VietQR status: " + paymentStatus.getStatus());
        }
        log.info("[PayOrderService] VietQR confirmed COMPLETED — creating PaymentTransaction");
        return orderFinalization.finalizeOrder(PaymentTransaction.of(
                "ORD" + order.getId(),
                Instant.now(),
                method().name(),
                order.getTotalAmount(),
                order
        ).getOrder());
    }
}
