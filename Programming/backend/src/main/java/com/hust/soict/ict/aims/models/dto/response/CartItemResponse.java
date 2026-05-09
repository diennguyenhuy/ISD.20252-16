package com.hust.soict.ict.aims.models.dto.response;

import com.hust.soict.ict.aims.models.dto.response.product.ProductSummary;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartItemResponse {
    private ProductSummary product;
    private int quantity;
    private long itemTotalPrice;
}
