package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductLogResponse extends ProductAuditLogResponse {
    private String action;
    private List<ProductEditDetailResponse> details;
}
