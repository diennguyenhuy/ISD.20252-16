package com.hust.soict.ict.aims.services.customer;

import com.hust.soict.ict.aims.services.notification.NotificationService;
import com.hust.soict.ict.aims.services.notification.email.OrderCancellationEmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderCancellationListener {
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderCancelEvent event) {
        notificationService.send(OrderCancellationEmailMessage.class, event.order());
    }
}
