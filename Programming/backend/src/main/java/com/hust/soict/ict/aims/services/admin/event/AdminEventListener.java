package com.hust.soict.ict.aims.services.admin.event;

import com.hust.soict.ict.aims.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AdminEventListener {
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(PasswordResetEvent event) {
        notificationService.send(PasswordResetEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(AccountCreatedEvent event) {
        notificationService.send(AccountCreatedEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(EmailUpdateEvent event) {
        notificationService.send(EmailUpdateAlertEmailMessage.class, event);
        notificationService.send(EmailUpdateConfirmationEmailMessage.class, event);
    }
}
