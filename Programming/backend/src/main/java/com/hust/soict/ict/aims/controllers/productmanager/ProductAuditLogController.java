package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.dto.response.audit.ProductAuditLogResponse;
import com.hust.soict.ict.aims.services.audit.ProductAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manager/logs")
@RequiredArgsConstructor
public class ProductAuditLogController {
    private final ProductAuditLogService productAuditLogService;

    @GetMapping
    public List<ProductAuditLogResponse> getProductAuditLogs() {
        return productAuditLogService.getProductAuditLogs();
    }
}
