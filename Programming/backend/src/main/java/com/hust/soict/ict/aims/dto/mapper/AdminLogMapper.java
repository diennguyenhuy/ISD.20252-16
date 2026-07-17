package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.AdminLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import org.springframework.stereotype.Component;

@Component
class AdminLogMapper extends AuditLogMapper<AdminLog, AdminLogResponse> {

    AdminLogMapper() {
        super(AdminLogResponse::new);
    }

    @Override
    public void map(AdminLog entity, AdminLogResponse target) {
        target.setAction(entity.getAction().name());
        target.setAdminId(entity.getAdminId());
        target.setAdminUsername(entity.getAdminUsername());
        target.setAffectedUserId(entity.getAffectedUserId());
        target.setAffectedUsername(entity.getAffectedUsername());
    }

    @Override
    public Class<AdminLog> getSourceClass() {
        return AdminLog.class;
    }

    @Override
    public Class<AdminLogResponse> getTargetClass() {
        return AdminLogResponse.class;
    }

}
