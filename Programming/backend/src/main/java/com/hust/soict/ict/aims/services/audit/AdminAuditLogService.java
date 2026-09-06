package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.models.entities.audit.AdminLog;

import java.util.List;
import java.util.UUID;

public interface AdminAuditLogService {
    List<AdminLog> getAdminAuditLogs();
    List<AdminLog> getAdminLogByUser(UUID userId);
}
