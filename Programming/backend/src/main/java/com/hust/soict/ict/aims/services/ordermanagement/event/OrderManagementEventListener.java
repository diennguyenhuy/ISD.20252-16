package com.hust.soict.ict.aims.services.ordermanagement.event;

import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import com.hust.soict.ict.aims.subsystems.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderManagementEventListener {
    private final NotificationService notificationService;
    private final RefundService refundService;
    private final OrderRepository orderRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderApprovalEvent event) {
        notificationService.send(OrderApprovalEmailMessage.class, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void handle(OrderRejectionEvent event) {
        var transaction = event.getOrder().getPaymentTransaction();
        if (refundService.supports(transaction.getTransactionMethod())) {
            event.setRefundable(true);
            try {
                refundService.refund(event.getOrder().getPaymentTransaction());
                event.getOrder().refund();
                orderRepository.save(event.getOrder());
            } catch (Exception e) {
                log.error("Unable to refund order {}, order remains REJECTED", event.getOrder().getId(), e);
            }
        }

        //No matter whether refundable or not, still send notification
        //Best-effort sending to prevent @Transaction rollback
        try {
            notificationService.send(OrderRejectionEmailMessage.class, event);
        } catch (Exception e) {
            log.error("Unable to send Order Rejection Email Message", e);
        }
    }
}
