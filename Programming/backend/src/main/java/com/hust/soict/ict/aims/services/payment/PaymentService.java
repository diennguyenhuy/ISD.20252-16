package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.order.OrderFinalization;

import java.time.Instant;

public abstract class PaymentService {
    private final OrderDraftContext orderDraftContext;
    private final OrderFinalization orderFinalization;

    protected PaymentService(OrderDraftContext orderDraftContext, OrderFinalization orderFinalization) {
        this.orderDraftContext = orderDraftContext;
        this.orderFinalization = orderFinalization;
    }

    public abstract PaymentMethod method();

    protected abstract PaymentInitiation startPayment() throws PaymentException;

    private PaymentTransaction generatePaymentTransaction(String transactionContent) {
        return PaymentTransaction.of(
                transactionContent,
                Instant.now(),
                method().name(),
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
