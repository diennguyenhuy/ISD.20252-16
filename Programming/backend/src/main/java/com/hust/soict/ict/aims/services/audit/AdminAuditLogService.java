package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.dto.response.audit.AdminLogResponse;

import java.util.List;

public interface AdminAuditLogService {
    List<AdminLogResponse> getAdminAuditLogs();
}
