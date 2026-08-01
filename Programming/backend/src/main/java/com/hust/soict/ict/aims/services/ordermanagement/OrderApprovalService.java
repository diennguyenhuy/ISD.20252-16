package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.exceptions.OrderApprovalException;

import java.util.UUID;

public interface OrderApprovalService {
    OrderResponse approveOrder(UUID id) throws OrderApprovalException;
}
