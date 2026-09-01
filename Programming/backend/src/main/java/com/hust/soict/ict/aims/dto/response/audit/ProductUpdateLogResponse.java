package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductUpdateLogResponse extends ProductLogResponse {
    private Map<String, AbstractProductEditDetailResponse> details;
}

interface AbstractProductEditDetailResponse {}

record ProductEditDetailResponse(
        String oldValue,
        String newValue
) implements AbstractProductEditDetailResponse {}

record ProductEditArrayDetailResponse(
        List<String> removedValues,
        List<String> addedValues
) implements AbstractProductEditDetailResponse {}