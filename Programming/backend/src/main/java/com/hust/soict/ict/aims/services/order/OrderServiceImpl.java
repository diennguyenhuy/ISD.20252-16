package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.dto.mapper.OrderMapper;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.services.order.event.OrderCancelEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public OrderResponse getOrder(UUID orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toOrderResponse(order);
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
}
