package com.hust.soict.ict.aims.dto.response.audit;

import java.util.List;

interface AbstractProductEditDetailResponse {
    String fieldName();
}

record ProductEditDetailResponse(
        String fieldName,
        String oldValue,
        String newValue
) implements AbstractProductEditDetailResponse {}

record ProductEditArrayDetailResponse(
        String fieldName,
        List<String> removedValues,
        List<String> addedValues
) implements AbstractProductEditDetailResponse {}