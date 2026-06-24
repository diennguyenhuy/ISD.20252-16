package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.dto.response.audit.ProductAuditLogResponse;

import java.util.List;

public interface ProductAuditLogService {
    List<ProductAuditLogResponse> getProductAuditLogs();
}
