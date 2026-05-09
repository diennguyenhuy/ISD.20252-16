package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ProductSummary {
    private String id;
    private String title;
    private long currentPrice;
    private String productType;
    private List<String> creators;
    private String imageURL;
}
