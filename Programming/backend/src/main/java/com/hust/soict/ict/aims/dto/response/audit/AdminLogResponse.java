package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminLogResponse extends AuditLogResponse {
    private UUID adminId;
    private String adminUsername;
    private String adminEmail;
    private UUID affectedUserId;
    private String affectedUsername;
    private String affectedUserEmail;
}
