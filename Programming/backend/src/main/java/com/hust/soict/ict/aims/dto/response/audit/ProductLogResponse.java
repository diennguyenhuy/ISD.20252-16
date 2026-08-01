package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductLogResponse extends AuditLogResponse {
    private String action;
    private UUID managerId;
    private String managerUsername;
    private UUID productId;
    private String productTitle;
}
