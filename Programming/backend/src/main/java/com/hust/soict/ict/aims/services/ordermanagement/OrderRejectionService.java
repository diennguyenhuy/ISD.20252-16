package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;

import java.util.UUID;

public interface OrderRejectionService {
    OrderResponse rejectOrder(UUID id);
}
