package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.OrderLog;
import org.springframework.stereotype.Component;

@Component
public class OrderLogMapper extends AuditLogMapper<OrderLog, OrderLogResponse> {
    OrderLogMapper() {
        super(OrderLog.class, OrderLogResponse::new);
    }

    @Override
    protected void map(OrderLog log, OrderLogResponse response) {
        response.setOrderId(log.getOrderId());
        log.getManagerId().ifPresent(response::setManagerId);
        log.getManagerUsername().ifPresent(response::setManagerUsername);
    }
}
