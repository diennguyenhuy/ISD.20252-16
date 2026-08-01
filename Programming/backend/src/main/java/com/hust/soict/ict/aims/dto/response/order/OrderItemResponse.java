package com.hust.soict.ict.aims.dto.response.order;

import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        UUID productReferenceId,
        String productName,
        String productImage,
        int quantity,
        long unitPrice,
        long itemTotalPrice
) {
    public OrderItemResponse(UUID productReferenceId, String productName, int quantity, long unitPrice, long itemTotalPrice) {
        this(null, productReferenceId, productName, null, quantity, unitPrice, itemTotalPrice);
    }
}
