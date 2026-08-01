package com.hust.soict.ict.aims.subsystems;

import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.order.OrderFinalization;

import java.time.Instant;

public abstract class PaymentService {
    private final OrderDraftContext orderDraftContext;
    private final OrderFinalization orderFinalization;
    private final PaymentMethod paymentMethod;

    protected PaymentService(PaymentMethod paymentMethod, OrderDraftContext orderDraftContext, OrderFinalization orderFinalization) {
        this.paymentMethod = paymentMethod;
        this.orderDraftContext = orderDraftContext;
        this.orderFinalization = orderFinalization;
    }

    public final PaymentMethod method() {
        return paymentMethod;
    }

    protected abstract PaymentInitiation startPayment() throws PaymentException;

    private PaymentTransaction generatePaymentTransaction(String transactionContent) {
        return PaymentTransaction.of(
                transactionContent,
                Instant.now(),
                paymentMethod.name(),
                currentOrder().getTotalAmount()
        );
    }

    protected final Order.Draft currentOrder() {
        return orderDraftContext.getDraftOrder();
    }

    protected final OrderResponse finalizePayment(String transactionContent) {
        currentOrder().attachPaymentTransaction(generatePaymentTransaction(transactionContent));
        return orderFinalization.finalizeOrder(currentOrder());
    }
}
