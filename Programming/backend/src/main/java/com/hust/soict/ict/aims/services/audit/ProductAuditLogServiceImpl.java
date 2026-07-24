package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.dto.mapper.Mapper;
import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
class ProductAuditLogServiceImpl implements ProductAuditLogService {
    private final ProductLogRepository productLogRepository;

    private final Mapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductLogResponse> getProductAuditLogs() {
        return productLogRepository.findTop100ByOrderByTimestampDesc().stream()
                .sorted(Comparator.comparing(ProductLog::getTimestamp).reversed())
                .limit(100)
                .map(this::map)
                .toList();
    }

    private ProductLogResponse map(ProductLog productLog) {
        return mapper.map(productLog, ProductLogResponse.class);
    }
}
