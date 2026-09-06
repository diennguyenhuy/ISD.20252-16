package com.hust.soict.ict.aims.dto.response.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductLogResponse extends AuditLogResponse {
    private UUID managerId;
    private String managerUsername;
    private UUID productId;
    private String productTitle;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AbstractProductEditDetailResponse> details;
}
