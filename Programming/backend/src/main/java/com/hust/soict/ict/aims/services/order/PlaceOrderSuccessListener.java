package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
class PlaceOrderSuccessListener {
    private final CartContext cartContext;
    private final OrderDraftContext orderDraftContext;

    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderSuccessEvent event) {
        try {
            notificationService.sendOrderConfirmation(event.getOrder(), event.getPaymentTransaction());
        } catch (Exception e) {
            System.err.println("Could not send email: " + e.getMessage());
        }

        orderDraftContext.clearDraftOrder();
        cartContext.getOrCreateCart().clear();
    }
}
