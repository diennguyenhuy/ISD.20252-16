package com.hust.soict.ict.aims.dto.response.product;

import java.util.List;
import java.util.UUID;

public record ProductSummary(
        UUID id,
        String title,
        long originalValue,
        long currentPrice,
        int stockQuantity,
        String productType,
        List<String> creators,
        String imageURL,
        String status
) {}
