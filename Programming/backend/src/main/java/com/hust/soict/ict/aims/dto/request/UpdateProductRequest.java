package com.hust.soict.ict.aims.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "productType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateBookRequest.class, name = "BOOK"),
        @JsonSubTypes.Type(value = UpdateCDRequest.class, name = "CD"),
        @JsonSubTypes.Type(value = UpdateDVDRequest.class, name = "DVD"),
        @JsonSubTypes.Type(value = UpdateNewspaperRequest.class, name = "NEWSPAPER")
})
@Data
public abstract class UpdateProductRequest {
    @NotBlank(message = "Product type is required (BOOK, CD, DVD, NEWSPAPER)")
    private String productType;
    @NullOrNotBlank(message = "Title must not be blank if provided")
    private String title;
    @NullOrNotBlank(message = "Category must not be blank if provided")
    private String category;
    @NullOrNotBlank(message = "Description must not be blank if provided")
    private String description;
    @NullOrNotBlank(message = "Image URL must not be blank if provided")
    private String imageURL;
    @Positive(message = "Height must be positive")
    private BigDecimal height;
    @Positive(message = "Width must be positive")
    private BigDecimal width;
    @Positive(message = "Length must be positive")
    private BigDecimal length;
    @Positive(message = "Weight must be positive")
    private BigDecimal weight;
    @PositiveOrZero(message = "Current price cannot be negative")
    private Integer currentPrice;
}
