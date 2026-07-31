package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.AdminLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import org.springframework.stereotype.Component;

@Component
class AdminLogMapper extends AuditLogMapper<AdminLog, AdminLogResponse> {

    AdminLogMapper() {
        super(AdminLog.class, AdminLogResponse.class, AdminLogResponse::new);
    }

    @Override
    protected void mapAuditLog(AdminLog source, AdminLogResponse target) {
        target.setAction(source.getAction().name());
        target.setAdminId(source.getAdminId());
        target.setAdminUsername(source.getAdminUsername());
        target.setAffectedUserId(source.getAffectedUserId());
        target.setAffectedUsername(source.getAffectedUsername());
    }
}
