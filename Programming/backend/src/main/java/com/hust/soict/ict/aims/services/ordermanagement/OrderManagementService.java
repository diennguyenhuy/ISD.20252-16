package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.exceptions.OrderApprovalException;
import com.hust.soict.ict.aims.exceptions.OrderRefundException;
import com.hust.soict.ict.aims.exceptions.OrderRejectionException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;
import com.hust.soict.ict.aims.models.entities.order.Order;

import java.util.UUID;

public interface OrderManagementService {
    Order approveOrder(UUID id) throws OrderApprovalException, OrderStateTransitionException;
    Order rejectOrder(UUID id) throws OrderRejectionException;
    Order refundOrder(UUID id) throws OrderRefundException;
}
