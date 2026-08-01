package com.hust.soict.ict.aims.dto.response.audit;

public record ProductEditDetailResponse(
        String fieldName,
        String oldValue,
        String newValue
) {}
