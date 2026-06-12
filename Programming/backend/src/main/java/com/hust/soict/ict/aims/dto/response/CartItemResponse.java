package com.hust.soict.ict.aims.dto.response;

import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartItemResponse {
    private ProductSummary product;
    private int quantity;
    private long itemTotalPrice;
}
