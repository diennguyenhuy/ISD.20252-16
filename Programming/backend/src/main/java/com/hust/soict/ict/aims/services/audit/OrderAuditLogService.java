package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.models.entities.audit.OrderLog;

import java.util.List;
import java.util.UUID;

public interface OrderAuditLogService {
    List<OrderLog> getOrderLogs();
    List<OrderLog> getOrderLogsByOrderId(UUID orderId);
}
