package com.hust.soict.ict.aims.models.dto.response.order;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class OrderItemResponse {
    private String productId;
    private String productName;
    private int quantity;
    private long unitPrice;
    private long itemTotalPrice;
}
