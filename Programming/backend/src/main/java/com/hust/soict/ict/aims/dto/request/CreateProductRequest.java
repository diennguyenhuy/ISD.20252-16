package com.hust.soict.ict.aims.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hust.soict.ict.aims.constraints.ValidPriceRange;
import com.hust.soict.ict.aims.models.entities.product.Product;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "productType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateBookRequest.class, name = "BOOK"),
        @JsonSubTypes.Type(value = CreateCDRequest.class, name = "CD"),
        @JsonSubTypes.Type(value = CreateDVDRequest.class, name = "DVD"),
        @JsonSubTypes.Type(value = CreateNewspaperRequest.class, name = "NEWSPAPER")
})
@Data
public abstract class CreateProductRequest {
    @NotBlank(message = "Product type is required (BOOK, CD, DVD, NEWSPAPER)")
    private String productType;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;
    private String description;

    @NotBlank(message = "Barcode is required")
    private String barcode;

    @NotNull
    @PositiveOrZero(message = "Original value cannot be negative")
    private Long originalValue;

    @NotNull
    @PositiveOrZero(message = "Current price cannot be negative")
    @ValidPriceRange(message = "Current price must be within "
            + Product.MIN_PRICE_RELATIVE_PERCENTAGE + "% and "
            + Product.MAX_PRICE_RELATIVE_PERCENTAGE + "% of original price"
    )
    private Long currentPrice;

    @NotNull
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private String imageURL;
    @NotNull
    @Positive(message = "Height must be positive")
    private BigDecimal height;
    @NotNull
    @Positive(message = "Width must be positive")
    private BigDecimal width;
    @NotNull
    @Positive(message = "Length must be positive")
    private BigDecimal length;
    @NotNull
    @Positive(message = "Weight must be positive")
    private BigDecimal weight;
}
