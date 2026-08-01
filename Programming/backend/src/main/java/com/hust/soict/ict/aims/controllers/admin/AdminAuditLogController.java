package com.hust.soict.ict.aims.controllers.admin;

import com.hust.soict.ict.aims.dto.response.audit.AdminLogResponse;
import com.hust.soict.ict.aims.services.audit.AdminAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/logs")
@RequiredArgsConstructor
public class AdminAuditLogController {
    private final AdminAuditLogService adminAuditLogService;

    @GetMapping
    public List<AdminLogResponse> getAdminLogs() {
        return adminAuditLogService.getAdminAuditLogs();
    }
}
