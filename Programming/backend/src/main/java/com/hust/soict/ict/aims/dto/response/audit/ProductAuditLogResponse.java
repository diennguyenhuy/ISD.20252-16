package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public abstract class ProductAuditLogResponse {
    private String id;
    private Instant timestamp;
    private UUID managerId;
    private String managerUsername;
    private UUID productId;
    private String productTitle;
}
