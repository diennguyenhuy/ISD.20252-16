package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@NoArgsConstructor
public abstract class ProductDetail {
    private String id;
    private String title;
    private String category;
    private String description;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal weight;
    private String barcode;
    private long currentPrice;
    private String status;
    private String imageURL;
    private Instant addedAt;
    private String productType;
}
