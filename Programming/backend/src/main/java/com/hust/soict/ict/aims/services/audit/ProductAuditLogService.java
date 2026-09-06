package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.models.entities.audit.ProductLog;

import java.util.List;
import java.util.UUID;

public interface ProductAuditLogService {
    List<ProductLog> getProductAuditLogs();
    List<ProductLog> getProductLogsByProductId(UUID productId);
}
