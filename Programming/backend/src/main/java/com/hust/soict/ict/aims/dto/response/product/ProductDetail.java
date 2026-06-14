package com.hust.soict.ict.aims.dto.response.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public abstract class ProductDetail {
    private UUID id;
    private String title;
    private String category;
    private String description;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal weight;
    private String barcode;
    private long originalValue;
    private long currentPrice;
    private int stockQuantity;
    private String status;
    private String imageURL;
    private Instant createdAt;
    private Instant updatedAt;
    private String productType;
}
