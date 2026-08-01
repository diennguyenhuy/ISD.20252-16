package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.dto.response.order.OrderMappers;
import com.hust.soict.ict.aims.exceptions.OrderNotCompleteException;
import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.services.order.event.OrderCancelEvent;
import com.hust.soict.ict.aims.services.order.event.OrderSuccessEvent;
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
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return OrderMappers.map(order);
    }

    @Override
    @Transactional
    public void cancelOrder(UUID orderId) throws OrderStateTransitionException, OrderNotFoundException {
        log.debug("Canceling order #{}...", orderId);
        Order order = orderRepository.findByIdAndStatus(orderId, Order.Status.PENDING)
                .orElseThrow(() -> new OrderNotFoundException(orderId, Order.Status.PENDING.name()));
        order.cancel();

        log.debug("Order #{} successfully canceled!", orderId);
        orderRepository.save(order);

        applicationEventPublisher.publishEvent(new OrderCancelEvent(order));
    }

    @Override
    @Transactional
    public OrderResponse finalizeOrder(Order.Draft draftOrder) throws OrderNotCompleteException {
        log.debug("Finalizing order...");

        Order order = draftOrder.complete();
        order = orderRepository.save(order);

        applicationEventPublisher.publishEvent(new OrderSuccessEvent(order));

        log.debug("Order has been successfully saved!");

        return OrderMappers.map(order);
    }
}
