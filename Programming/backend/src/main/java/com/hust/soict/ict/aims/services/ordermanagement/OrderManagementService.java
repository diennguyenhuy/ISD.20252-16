package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.exceptions.OrderApprovalException;
import com.hust.soict.ict.aims.models.entities.order.Order;

import java.util.UUID;

public interface OrderManagementService {
    Order approveOrder(UUID id) throws OrderApprovalException;
    Order rejectOrder(UUID id);
    Order refundOrder(UUID id);
}
