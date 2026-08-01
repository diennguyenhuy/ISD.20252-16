package com.hust.soict.ict.aims.dto.response.cart;

import com.hust.soict.ict.aims.dto.response.product.ProductSummary;

public record CartItemResponse(
        ProductSummary product,
        int quantity,
        long itemTotalPrice
) {}
