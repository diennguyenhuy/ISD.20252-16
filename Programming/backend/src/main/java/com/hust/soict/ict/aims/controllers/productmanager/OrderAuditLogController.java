package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.dto.response.audit.OrderLogMapper;
import com.hust.soict.ict.aims.dto.response.audit.OrderLogResponse;
import com.hust.soict.ict.aims.services.audit.OrderAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderAuditLogController {
    private final OrderAuditLogService orderAuditLogService;
    private final OrderLogMapper orderLogMapper;

    @GetMapping("/manager/orders/logs")
    public List<OrderLogResponse> getOrderAuditLogs() {
        return orderAuditLogService.getOrderLogs().stream()
                .map(orderLogMapper::map)
                .toList();
    }

    @GetMapping("order/history/{orderId}")
    public List<OrderLogResponse> getOrderAuditLogs(@PathVariable UUID orderId) {
        return orderAuditLogService.getOrderLogsByOrderId(orderId).stream()
                .map(orderLogMapper::map)
                .toList();
    }
}
