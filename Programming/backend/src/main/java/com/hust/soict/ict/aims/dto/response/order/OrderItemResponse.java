package com.hust.soict.ict.aims.dto.response.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        UUID productReferenceId,
        String productTitle,
        String productImage,
        String productType,
        int quantity,
        long unitPrice,
        long itemTotalPrice,
        BigDecimal unitWeight,
        BigDecimal itemTotalWeight
) {}
