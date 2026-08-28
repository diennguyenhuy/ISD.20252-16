package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderQueryService {
    Page<Order> getPendingOrders(Pageable pageable);
    Page<Order> getOrders(Order.Status status, Pageable pageable);
    Order getOrderById(UUID id);
}
