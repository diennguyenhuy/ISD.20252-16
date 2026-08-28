package com.hust.soict.ict.aims.subsystems.vietqr.service;

import com.hust.soict.ict.aims.subsystems.PaymentMethod;
import com.hust.soict.ict.aims.subsystems.PaymentService;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.order.OrderFinalization;
import com.hust.soict.ict.aims.subsystems.vietqr.VietQRPaymentGateway;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRPaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class VietQRPaymentServiceImpl extends PaymentService implements VietQRPaymentService {
    private final VietQRPaymentGateway qrPaymentGateway;

    public VietQRPaymentServiceImpl(VietQRPaymentGateway qrPaymentGateway, OrderFinalization orderFinalization, OrderDraftContext orderDraftContext) {
        super(PaymentMethod.VIETQR, orderDraftContext, orderFinalization);
        this.qrPaymentGateway = qrPaymentGateway;
    }

    @Override
    protected VietQRCode startPayment() throws PaymentException {
        return qrPaymentGateway.generateQRCode(currentOrder().checkoutId.toString(), currentOrder().getTotalAmount());
    }

    @Override
    public VietQRPaymentStatus checkPaymentStatus() throws PaymentException {
        return qrPaymentGateway.checkPaymentStatus(currentOrder().checkoutId.toString(), currentOrder().getTotalAmount());
    }

    @Override
    public Order confirmPayment() throws PaymentException {
        Order.Draft order = currentOrder();

        VietQRPaymentStatus paymentStatus = qrPaymentGateway.checkPaymentStatus(order.checkoutId.toString(), order.getTotalAmount());
        if (!paymentStatus.isCompleted()) {
            throw new PaymentException("Payment verification failed. VietQR status: " + paymentStatus.getStatus());
        }
        log.info("[PayOrderService] VietQR confirmed COMPLETED — creating PaymentTransaction");
        return finalizePayment("ORD" + order.checkoutId);
    }
}
