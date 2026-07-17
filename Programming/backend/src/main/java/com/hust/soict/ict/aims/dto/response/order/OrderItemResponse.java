package com.hust.soict.ict.aims.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderItemResponse {
    private UUID productId;
    private UUID productReferenceId;
    private String productName;
    private String productImage;
    private int quantity;
    private long unitPrice;
    private long itemTotalPrice;
}
