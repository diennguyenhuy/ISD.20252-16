package com.hust.soict.ict.aims.controllers.admin;

import com.hust.soict.ict.aims.dto.response.audit.AdminLogMapper;
import com.hust.soict.ict.aims.dto.response.audit.AdminLogResponse;
import com.hust.soict.ict.aims.services.audit.AdminAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/logs")
@RequiredArgsConstructor
public class AdminAuditLogController {
    private final AdminAuditLogService adminAuditLogService;
    private final AdminLogMapper adminLogMapper;

    @GetMapping
    public List<AdminLogResponse> getAdminLogs() {
        return adminAuditLogService.getAdminAuditLogs().stream()
                .map(adminLogMapper::map)
                .toList();
    }

    @GetMapping("/{userId}")
    public List<AdminLogResponse> getAdminLogsByUserId(@PathVariable UUID userId) {
        return adminAuditLogService.getAdminLogByUser(userId).stream()
                .map(adminLogMapper::map)
                .toList();
    }
}
