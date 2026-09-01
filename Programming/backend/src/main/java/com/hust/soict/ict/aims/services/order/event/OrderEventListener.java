package com.hust.soict.ict.aims.services.order.event;

import com.hust.soict.ict.aims.models.entities.audit.OrderActions;
import com.hust.soict.ict.aims.models.entities.audit.OrderLog;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.repositories.OrderLogRepository;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Uses event-driven architecture and dependency inversion
 * to reduce direct coupling with order placement workflow.
 */
@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final NotificationService notificationService;
    private final OrderLogRepository orderLogRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void notify(OrderSuccessEvent event) {
        notificationService.send(OrderConfirmationEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(OrderSuccessEvent event) {
        OrderLog orderLog = OrderActions.PLACE.log(event.order());
        orderLogRepository.save(orderLog);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void notify(OrderCancelEvent event) {
        notificationService.send(OrderCancellationEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(OrderCancelEvent event) {
        OrderLog log;
        if (event.order().getStatus() == Order.Status.REFUNDED) {
            log = OrderActions.CANCEL_REFUND.log(event.order());
        } else {
            log = OrderActions.CANCEL.log(event.order());
        }
        orderLogRepository.save(log);
    }
}
