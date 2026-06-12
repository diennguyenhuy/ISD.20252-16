package com.hust.soict.ict.aims.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import com.hust.soict.ict.aims.constraints.ValidPriceRange;
import static com.hust.soict.ict.aims.models.entities.product.Product.MIN_PRICE_RELATIVE_PERCENTAGE;
import static com.hust.soict.ict.aims.models.entities.product.Product.MAX_PRICE_RELATIVE_PERCENTAGE;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "productType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateBookRequest.class, name = "Book"),
        @JsonSubTypes.Type(value = CreateCDRequest.class, name = "CD"),
        @JsonSubTypes.Type(value = CreateDVDRequest.class, name = "DVD"),
        @JsonSubTypes.Type(value = CreateNewspaperRequest.class, name = "Newspaper")
})
@Data
@ValidPriceRange(
        message = "Current price must be within "
        + MIN_PRICE_RELATIVE_PERCENTAGE + "% and "
        + MAX_PRICE_RELATIVE_PERCENTAGE + "% of original price"
)
public abstract class CreateProductRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Category is required")
    private String category;
    @NullOrNotBlank(message = "Description must not be blank if provided")
    private String description;
    @NotBlank(message = "Barcode is required")
    private String barcode;
    @NotNull(message = "Original value is required")
    @PositiveOrZero(message = "Original value cannot be negative")
    private Long originalValue;
    @NotNull(message = "Current price is required")
    @PositiveOrZero(message = "Current price cannot be negative")
    private Long currentPrice;
    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
    @NullOrNotBlank(message = "Image URL must not be blank if provided")
    private String imageURL;
    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private BigDecimal height;
    @NotNull(message = "Width is required")
    @Positive(message = "Width must be positive")
    private BigDecimal width;
    @NotNull(message = "Length is required")
    @Positive(message = "Length must be positive")
    private BigDecimal length;
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private BigDecimal weight;
}
