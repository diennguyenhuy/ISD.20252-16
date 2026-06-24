package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;

import java.util.UUID;

public interface OrderService {
    OrderResponse getOrder(UUID orderId) throws OrderNotFoundException;
    void cancelOrder(UUID orderId) throws OrderStateTransitionException, OrderNotFoundException;
}
