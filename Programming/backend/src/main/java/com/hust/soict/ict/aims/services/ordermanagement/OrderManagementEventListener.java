package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.services.notification.NotificationService;
import com.hust.soict.ict.aims.services.notification.email.OrderApprovalEmailMessage;
import com.hust.soict.ict.aims.services.notification.email.OrderRejectionEmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderManagementEventListener {
    private final NotificationService notificationService;
    //TODO: REFUND PAYPAL

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderApprovalEvent event) {
        notificationService.send(OrderApprovalEmailMessage.class, event.order());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderRejectionEvent event) {
        notificationService.send(OrderRejectionEmailMessage.class, event.order());
    }
}
