package com.hust.soict.ict.aims.models.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderItemResponse {
    private String productId;
    private String productName;
    private int quantity;
    private long unitPrice;
    private long itemTotalPrice;
}
