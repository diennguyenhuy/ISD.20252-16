package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.OrderCancellationException;
import com.hust.soict.ict.aims.exceptions.OrderNotCompleteException;
import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.services.order.event.OrderCancelEvent;
import com.hust.soict.ict.aims.services.order.event.OrderSuccessEvent;
import com.hust.soict.ict.aims.subsystems.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class OrderServiceImpl implements OrderService, OrderFinalization {
    private final OrderRepository orderRepository;
    private final RefundService refundService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(readOnly = true)
    public Order getOrder(UUID orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    @Transactional
    public Order cancelOrder(UUID orderId) throws OrderCancellationException, OrderNotFoundException {
        log.debug("Canceling order #{}...", orderId);
        Order order = orderRepository.findByIdAndStatus(orderId, Order.Status.PENDING)
                .orElseThrow(() -> new OrderNotFoundException(orderId, Order.Status.PENDING.name()));
        boolean refundable = refundService.supports(order.getPaymentTransaction().getTransactionMethod());
        try {
            order.cancel(refundService::refund);
        } catch (OrderStateTransitionException e) {
            throw new OrderCancellationException(orderId, e);
        }

        log.debug("Order #{} successfully canceled!", orderId);
        orderRepository.save(order);

        applicationEventPublisher.publishEvent(new OrderCancelEvent(order, refundable));

        return order;
    }

    @Override
    @Transactional
    public Order finalizeOrder(Order.Draft draftOrder) throws OrderNotCompleteException {
        log.debug("Finalizing order...");

        Order order = draftOrder.complete();
        order = orderRepository.save(order);

        applicationEventPublisher.publishEvent(new OrderSuccessEvent(order));

        log.debug("Order has been successfully saved!");

        return order;
    }
}
