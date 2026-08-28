package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;
import com.hust.soict.ict.aims.models.entities.order.Order;

import java.util.UUID;

public interface OrderService {
    Order getOrder(UUID orderId) throws OrderNotFoundException;
    Order cancelOrder(UUID orderId) throws OrderStateTransitionException, OrderNotFoundException;
}
