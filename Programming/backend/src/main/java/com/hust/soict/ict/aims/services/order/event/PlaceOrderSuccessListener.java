package com.hust.soict.ict.aims.services.order.event;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Uses event-driven architecture and dependency inversion
 * to reduce direct coupling with order placement workflow.
 */
@Component
@RequiredArgsConstructor
public class PlaceOrderSuccessListener {
    private final CartContext cartContext;
    private final OrderDraftContext orderDraftContext;
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderSuccessEvent event) {
        cartContext.getOrCreateCart().clear();
        orderDraftContext.clearDraftOrder();
        notificationService.send(OrderConfirmationEmailMessage.class, event);
    }
}
