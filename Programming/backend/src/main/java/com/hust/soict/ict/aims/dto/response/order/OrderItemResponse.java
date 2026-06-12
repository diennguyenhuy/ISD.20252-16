package com.hust.soict.ict.aims.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderItemResponse {
    private String productId;
    private String productName;
    private String productImage;
    private int quantity;
    private long unitPrice;
    private long itemTotalPrice;
}
