package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.dto.mapper.Mapper;
import com.hust.soict.ict.aims.dto.response.audit.ProductAuditLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductAuditLog;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import com.hust.soict.ict.aims.repositories.StockAdjustLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
class ProductAuditLogServiceImpl implements ProductAuditLogService {
    private final ProductLogRepository productLogRepository;
    private final StockAdjustLogRepository stockAdjustLogRepository;

    private final Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductAuditLogResponse> getProductAuditLogs() {
        List<ProductLog> productLogs = productLogRepository.findTop100ByOrderByTimestampDesc();
        List<StockAdjustLog> stockAdjustLogs = stockAdjustLogRepository.findTop100ByOrderByTimestampDesc();

        List<ProductAuditLog> logs = new ArrayList<>();
        logs.addAll(productLogs);
        logs.addAll(stockAdjustLogs);

        return logs.stream()
                .sorted(Comparator.comparing(ProductAuditLog::getTimestamp).reversed())
                .limit(100)
                .map(this::map)
                .toList();
    }

    private ProductAuditLogResponse map(ProductAuditLog productAuditLog) {
        return mapper.map(productAuditLog, ProductAuditLogResponse.class);
    }
}
