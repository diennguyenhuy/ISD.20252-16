package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class AuditLogResponse {
    private UUID id;
    private Instant timestamp;
}
