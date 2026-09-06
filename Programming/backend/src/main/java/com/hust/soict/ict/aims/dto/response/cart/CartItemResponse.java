package com.hust.soict.ict.aims.dto.response.cart;

import java.util.UUID;

public record CartItemResponse(
        UUID productId,
        String productTitle,
        int stockQuantity,
        String productImage,
        String productType,
        int quantity,
        long unitPrice,
        long itemTotalPrice
) {}
