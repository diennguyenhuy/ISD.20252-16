package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.models.entities.audit.OrderLog;
import com.hust.soict.ict.aims.repositories.OrderLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class OrderAuditLogServiceImpl implements OrderAuditLogService {
    private final OrderLogRepository orderLogRepository;
    private static final int MAX_LOG = 100;

    @Override
    public List<OrderLog> getOrderLogs() {
        return orderLogRepository.findAll().stream()
                .sorted(Comparator.comparing(OrderLog::getTimestamp).reversed())
                .limit(MAX_LOG)
                .toList();
    }

    @Override
    public List<OrderLog> getOrderLogsByOrderId(UUID orderId) {
        return orderLogRepository.findByOrderId(orderId);
    }
}
