package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
public class ProductSummary {
    private String id;
    private String title;
    private long originalValue;
    private long currentPrice;
    private int stockQuantity;
    private String productType;
    private List<String> creators;
    private String imageURL;
}
