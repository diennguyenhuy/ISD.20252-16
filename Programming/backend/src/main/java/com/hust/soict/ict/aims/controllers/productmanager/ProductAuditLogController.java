package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.dto.response.audit.ProductLogMappers;
import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;
import com.hust.soict.ict.aims.services.audit.ProductAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/manager/logs")
@RequiredArgsConstructor
public class ProductAuditLogController {
    private final ProductAuditLogService productAuditLogService;
    private final ProductLogMappers productLogMapper;

    @GetMapping
    public List<ProductLogResponse> getProductAuditLogs() {
        return productAuditLogService.getProductAuditLogs().stream()
                .<ProductLogResponse>map(productLogMapper::map)
                .toList();
    }

    @GetMapping("/{productId}")
    public List<ProductLogResponse> getProductAuditLogs(@PathVariable UUID productId) {
        return productAuditLogService.getProductLogsByProductId(productId).stream()
                .<ProductLogResponse>map(productLogMapper::map)
                .toList();
    }
}
