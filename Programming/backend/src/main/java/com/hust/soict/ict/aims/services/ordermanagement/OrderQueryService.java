package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderQueryService {
    Page<OrderResponse> getPendingOrders(Pageable pageable);
    Page<OrderResponse> getOrders(Order.Status status, Pageable pageable);
    OrderResponse getOrderById(UUID id);
}
