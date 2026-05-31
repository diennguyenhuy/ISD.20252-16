package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ProductSummary {
    private String id;
    private String title;
    private long originalValue;
    private long currentPrice;
    private int stockQuantity;
    private String productType;
    private List<String> creators;
    private String imageURL;
    private String status;
}
