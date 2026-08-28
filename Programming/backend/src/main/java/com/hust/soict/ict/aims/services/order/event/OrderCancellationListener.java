package com.hust.soict.ict.aims.services.order.event;

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
class OrderCancellationListener {
    private final NotificationService notificationService;
    private final RefundService refundService;
    private final OrderRepository orderRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void handle(OrderCancelEvent event) {
        var transaction = event.order.getPaymentTransaction();
        if (refundService.supports(transaction.getTransactionMethod())) {
            event.refundable = true;
            try {
                refundService.refund(event.order.getPaymentTransaction());
                event.order.refund();
                orderRepository.save(event.order);
            } catch (Exception e) {
                log.error("Unable to refund order {}, order remains CANCELLED", event.order.getId(), e);
            }
        }

        //No matter whether refundable or not, still send notification
        //Best-effort sending to prevent @Transaction rollback
        try {
            notificationService.send(OrderCancellationEmailMessage.class, event);
        } catch (Exception e) {
            log.error("Unable to send Order Cancellation Email Message", e);
        }
    }
}
