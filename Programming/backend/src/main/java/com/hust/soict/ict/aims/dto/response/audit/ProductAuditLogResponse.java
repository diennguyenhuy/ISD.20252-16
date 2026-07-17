package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ProductAuditLogResponse extends AuditLogResponse {
    private UUID managerId;
    private String managerUsername;
    private UUID productId;
    private String productTitle;
}
