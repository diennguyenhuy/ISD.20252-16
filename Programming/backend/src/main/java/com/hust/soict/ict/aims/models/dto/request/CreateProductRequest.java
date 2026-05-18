package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Barcode cannot be blank")
    private String barcode;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotNull(message = "Original value is required")
    @PositiveOrZero
    private Long originalValue;

    @NotNull(message = "Current price is required")
    @PositiveOrZero
    private Long currentPrice;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero
    private Integer stockQuantity;

    private Double weight;
    private Double height;
    private Double width;
    private Double length;
    private String description;
}