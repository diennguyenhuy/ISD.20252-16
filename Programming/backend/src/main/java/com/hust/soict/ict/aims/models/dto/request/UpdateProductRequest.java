package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateProductRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Current price is required")
    @PositiveOrZero
    private Long currentPrice;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero
    private Integer stockQuantity;

    @NotBlank(message = "Description is required")
    private String description;
}