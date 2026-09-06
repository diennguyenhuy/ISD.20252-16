package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderLogResponse extends AuditLogResponse {
    private UUID orderId;
    private UUID managerId;
    private String managerUsername;
}
