package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import org.springframework.stereotype.Component;

@Component
public class AdminLogMapper extends AuditLogMapper<AdminLog, AdminLogResponse> {
    AdminLogMapper() {
        super(AdminLog.class, AdminLogResponse::new);
    }

    @Override
    protected void map(AdminLog log, AdminLogResponse response) {
        response.setAction(log.getAction().name());
        response.setAdminId(log.getAdminId());
        response.setAdminUsername(log.getAdminUsername());
        response.setAffectedUserId(log.getAffectedUserId());
        response.setAffectedUsername(log.getAffectedUsername());
    }
}
