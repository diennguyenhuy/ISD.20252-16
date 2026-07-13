package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.models.entities.audit.AdminLog;

import java.util.List;

public interface AdminAuditLogService {
    List<AdminLog> getAdminAuditLogs();
}
