package com.hust.soict.ict.aims.services.admin.event;

import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import com.hust.soict.ict.aims.models.entities.audit.UserAction;
import com.hust.soict.ict.aims.repositories.AdminLogRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
class AdminEventListener {
    private final NotificationService notificationService;
    private final AuthenticationFacade authenticationFacade;
    private final AdminLogRepository adminLogRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void notify(PasswordResetEvent event) {
        notificationService.send(PasswordResetEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(PasswordResetEvent event) {
        AdminLog log = UserAction.RESET_PASSWORD.log(authenticationFacade.getCurrentUser(), event.passwordResetUser());
        adminLogRepository.save(log);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void notify(AccountCreatedEvent event) {
        notificationService.send(AccountCreatedEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(AccountCreatedEvent event) {
        AdminLog log = UserAction.CREATE.log(authenticationFacade.getCurrentUser(), event.createdUser());
        adminLogRepository.save(log);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void notify(EmailUpdateEvent event) {
        notificationService.send(EmailUpdateAlertEmailMessage.class, event);
        notificationService.send(EmailUpdateConfirmationEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(EmailUpdateEvent event) {
        AdminLog log = UserAction.UPDATE_EMAIL.log(authenticationFacade.getCurrentUser(), event.emailUpdatedUser());
        adminLogRepository.save(log);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(AccountActivationEvent event) {
        AdminLog log;
        if (event.isDeactivate()) {
            log = UserAction.DEACTIVATE.log(authenticationFacade.getCurrentUser(), event.user());
        } else {
            log = UserAction.ACTIVATE.log(authenticationFacade.getCurrentUser(), event.user());
        }
        adminLogRepository.save(log);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(AccountBlockEvent event) {
        AdminLog log;
        if (event.isUnblock()) {
            log = UserAction.UNBLOCK.log(authenticationFacade.getCurrentUser(), event.user());
        } else {
            log = UserAction.BLOCK.log(authenticationFacade.getCurrentUser(), event.user());
        }
        adminLogRepository.save(log);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(UserRoleAssignOrModifyEvent event) {
        AdminLog log = UserAction.MODIFY_ROLE.log(authenticationFacade.getCurrentUser(), event.user());
        adminLogRepository.save(log);
    }
}
