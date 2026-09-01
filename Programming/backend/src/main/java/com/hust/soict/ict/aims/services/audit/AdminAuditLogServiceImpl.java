package com.hust.soict.ict.aims.services.audit;

import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import com.hust.soict.ict.aims.repositories.AdminLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class AdminAuditLogServiceImpl implements AdminAuditLogService {
    private final AdminLogRepository adminLogRepository;

    @Override
    public List<AdminLog> getAdminAuditLogs() {
        return adminLogRepository.findAll().stream()
                .sorted(Comparator.comparing(AdminLog::getTimestamp).reversed())
                .limit(MAX_LOG)
                .toList();
    }

    @Override
    public List<AdminLog> getAdminLogByUser(UUID userId) {
        return adminLogRepository.findAllByAdminId(userId).stream()
                .sorted(Comparator.comparing(AdminLog::getTimestamp).reversed())
                .limit(MAX_LOG)
                .toList();
    }
}
