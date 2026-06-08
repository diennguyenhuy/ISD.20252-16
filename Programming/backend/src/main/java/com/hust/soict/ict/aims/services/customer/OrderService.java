package com.hust.soict.ict.aims.services.customer;

import com.hust.soict.ict.aims.dto.mapper.OrderMapper;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId) throws OrderStateTransitionException, OrderNotFoundException {
        log.debug("Canceling order #{}...", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.cancel();

        log.debug("Order #{} successfully canceled!", orderId);
        orderRepository.save(order);

        applicationEventPublisher.publishEvent(new OrderCancelEvent(order));
    }
}
