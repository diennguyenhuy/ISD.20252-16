package com.hust.soict.ict.aims.subsystems.vietqr.service;

import com.hust.soict.ict.aims.services.payment.PaymentInitiation;
import com.hust.soict.ict.aims.services.payment.PaymentMethod;
import com.hust.soict.ict.aims.services.payment.PaymentService;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.order.OrderFinalization;
import com.hust.soict.ict.aims.subsystems.vietqr.VietQRPaymentGateway;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRPaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
@Slf4j
class VietQRPaymentServiceImpl extends PaymentService implements VietQRPaymentService {
    private final VietQRPaymentGateway qrPaymentGateway;

    public VietQRPaymentServiceImpl(VietQRPaymentGateway qrPaymentGateway, OrderFinalization orderFinalization, OrderDraftContext orderDraftContext) {
        super(orderDraftContext, orderFinalization);
        this.qrPaymentGateway = qrPaymentGateway;
    }

    @Override
    public PaymentMethod method() {
        return PaymentMethod.VIETQR;
    }

    @Override
    protected PaymentInitiation startPayment() throws PaymentException {
        return qrPaymentGateway.generateQRCode(currentOrder().getCheckoutId().toString(), currentOrder().getTotalAmount());
    }

    @Override
    public VietQRPaymentStatus checkPaymentStatus() throws PaymentException {
        return qrPaymentGateway.checkPaymentStatus(currentOrder().getCheckoutId().toString(), currentOrder().getTotalAmount());
    }

    @Override
    public OrderResponse confirmPayment() throws PaymentException {
        Order.Draft order = currentOrder();

        VietQRPaymentStatus paymentStatus = qrPaymentGateway.checkPaymentStatus(order.getCheckoutId().toString(), order.getTotalAmount());
        if (!paymentStatus.isCompleted()) {
            throw new PaymentException("Payment verification failed. VietQR status: " + paymentStatus.getStatus());
        }
        log.info("[PayOrderService] VietQR confirmed COMPLETED — creating PaymentTransaction");
        return finalizePayment("ORD" + order.getCheckoutId());
    }
}
