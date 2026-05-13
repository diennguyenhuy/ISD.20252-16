package com.hust.soict.ict.aims.models.dto.response;

import com.hust.soict.ict.aims.models.dto.response.product.ProductSummary;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class CartItemResponse {
    private ProductSummary product;
    private int quantity;
    private long itemTotalPrice;
}
