package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.order.OrderFinalization;

public abstract class PaymentService {
    private final OrderDraftContext orderDraftContext;
    private final OrderFinalization orderFinalization;

    protected PaymentService(OrderDraftContext orderDraftContext, OrderFinalization orderFinalization) {
        this.orderDraftContext = orderDraftContext;
        this.orderFinalization = orderFinalization;
    }

    public abstract PaymentMethod method();

    protected abstract PaymentTransaction generatePaymentTransaction(String transactionContent);

    protected final Order currentOrder() {
        return orderDraftContext.getDraftOrder();
    }

    protected final OrderResponse finalizeOrder(PaymentTransaction paymentTransaction) {
        return orderFinalization.finalizeOrder(paymentTransaction.getOrder());
    }
}
