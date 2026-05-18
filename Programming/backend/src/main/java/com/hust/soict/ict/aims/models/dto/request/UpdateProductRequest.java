package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateProductRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Current price is required")
    @PositiveOrZero
    private Long currentPrice;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero
    private Integer stockQuantity;

    private String description;
    private Double weight;
    private Double height;
    private Double width;
    private Double length;
}