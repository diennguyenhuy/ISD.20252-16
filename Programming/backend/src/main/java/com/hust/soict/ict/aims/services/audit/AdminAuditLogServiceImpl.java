package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.dto.mapper.Mapper;
import com.hust.soict.ict.aims.dto.response.audit.AdminLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import com.hust.soict.ict.aims.repositories.AdminLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
class AdminAuditLogServiceImpl implements AdminAuditLogService {
    private final AdminLogRepository adminLogRepository;
    private final Mapper mapper;

    @Override
    public List<AdminLogResponse> getAdminAuditLogs() {
        return adminLogRepository.findAll().stream()
                .sorted(Comparator.comparing(AdminLog::getTimestamp).reversed())
                .limit(100)
                .map(this::map)
                .toList();
    }

    private AdminLogResponse map(AdminLog adminLog) {
        return mapper.map(adminLog, AdminLogResponse.class);
    }
}
