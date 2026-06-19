package com.hust.soict.ict.aims.services.customer.event;

import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import com.hust.soict.ict.aims.services.payment.RefundRegistry;
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
    private final RefundRegistry refundRegistry;
    private final OrderRepository orderRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void handleRefund(OrderCancelEvent event) {
        var transaction = event.order().getPaymentTransaction();
        if (refundRegistry.supports(transaction.getTransactionMethod())) {
            try {
                refundRegistry.refund(event.order().getPaymentTransaction());
                event.order().refund();
                orderRepository.save(event.order());
            } catch (Exception e) {
                log.error("Unable to refund order {}, order remains CANCELLED", event.order().getId(), e);
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderCancelEvent event) {
        notificationService.send(OrderCancellationEmailMessage.class, event);
    }
}
