package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class ProductAuditLogServiceImpl implements ProductAuditLogService {
    private final ProductLogRepository productLogRepository;
    private static final int MAX_LOG = 100;

    @Override
    @Transactional(readOnly = true)
    public List<ProductLog> getProductAuditLogs() {
        return productLogRepository.findAll().stream()
                .sorted(Comparator.comparing(ProductLog::getTimestamp).reversed())
                .limit(MAX_LOG)
                .toList();
    }

    @Override
    public List<ProductLog> getProductLogsByProductId(UUID productId) {
        return productLogRepository.findByProductId(productId).stream()
                .sorted(Comparator.comparing(ProductLog::getTimestamp).reversed())
                .limit(MAX_LOG/5)
                .toList();
    }
}
