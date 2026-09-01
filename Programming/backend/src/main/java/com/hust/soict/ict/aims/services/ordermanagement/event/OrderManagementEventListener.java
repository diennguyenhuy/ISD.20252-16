package com.hust.soict.ict.aims.services.ordermanagement.event;

import com.hust.soict.ict.aims.models.entities.audit.OrderActions;
import com.hust.soict.ict.aims.models.entities.audit.OrderLog;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.repositories.OrderLogRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderManagementEventListener {
    private final NotificationService notificationService;
    private final AuthenticationFacade authenticationFacade;
    private final OrderLogRepository orderLogRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void notify(OrderApprovalEvent event) {
        notificationService.send(OrderApprovalEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(OrderApprovalEvent event) {
        OrderLog log = OrderActions.APPROVE.log(event.order(), authenticationFacade.getCurrentUser());
        orderLogRepository.save(log);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void notify(OrderRejectionEvent event) {
        notificationService.send(OrderRejectionEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(OrderRejectionEvent event) {
        OrderLog log;
        if (event.order().getStatus() == Order.Status.REFUNDED) {
            log = OrderActions.REJECT_REFUND.log(event.order(), authenticationFacade.getCurrentUser());
        } else {
            log = OrderActions.REJECT.log(event.order(), authenticationFacade.getCurrentUser());
        }
        orderLogRepository.save(log);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void notify(OrderRefundEvent event) {
        notificationService.send(OrderRefundEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void log(OrderRefundEvent event) {
        OrderLog log = OrderActions.REFUND.log(event.order(), authenticationFacade.getCurrentUser());
        orderLogRepository.save(log);
    }
}
