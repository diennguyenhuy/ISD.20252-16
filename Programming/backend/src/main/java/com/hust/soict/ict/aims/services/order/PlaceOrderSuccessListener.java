package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.services.notification.NotificationMethod;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import com.hust.soict.ict.aims.services.notification.email.OrderConfirmationEmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Uses event-driven architecture and dependency inversion
 * to reduce direct coupling with order placement workflow.
 * Cohesion: Communicational Cohesion
 * Reason:
 * All operations react to the same OrderSuccessEvent
 * and process related post-order data and side effects.
 * Coupling:
 * - Data coupling with NotificationService through
 *   interface-based method calls.
 * - Stamp coupling with OrderSuccessEvent and CartContext
 *   because composite objects are passed/shared.
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
        notificationService.send(NotificationMethod.EMAIL, OrderConfirmationEmailMessage.class, event.order());
    }
}
