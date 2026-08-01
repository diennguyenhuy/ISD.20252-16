package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;

import java.util.List;

public interface ProductAuditLogService {
    List<ProductLogResponse> getProductAuditLogs();
}
