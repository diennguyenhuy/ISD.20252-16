package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;

@Data
public class ProductEditDetailResponse {
    private String fieldName;
    private String oldValue;
    private String newValue;
}
