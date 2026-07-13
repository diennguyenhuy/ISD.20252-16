package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.dto.mapper.OrderMapper;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.exceptions.OrderNotCompleteException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.services.order.event.OrderSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class OrderFinalizer implements OrderFinalization {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public OrderResponse finalizeOrder(Order.Draft draftOrder) throws OrderNotCompleteException {
        log.debug("Finalizing order...");

        Order order = draftOrder.complete();
        order = orderRepository.save(order);

        applicationEventPublisher.publishEvent(new OrderSuccessEvent(order));

        log.debug("Order has been successfully saved!");

        return orderMapper.toOrderResponse(order);
    }
}
