package com.hust.soict.ict.aims.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "productType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateBookRequest.class, name = "Book"),
        @JsonSubTypes.Type(value = UpdateCDRequest.class, name = "CD"),
        @JsonSubTypes.Type(value = UpdateDVDRequest.class, name = "DVD"),
        @JsonSubTypes.Type(value = UpdateNewspaperRequest.class, name = "Newspaper")
})
@Data
public abstract class UpdateProductRequest {
    @NullOrNotBlank(message = "Title must not be blank if provided")
    private String title;
    @NullOrNotBlank(message = "Category must not be blank if provided")
    private String category;
    @NullOrNotBlank(message = "Description must not be blank if provided")
    private String description;
    @NullOrNotBlank(message = "Image URL must not be blank if provided")
    private String imageURL;
    @Positive(message = "Height must be positive if provided")
    private BigDecimal height;
    @Positive(message = "Width must be positive if provided")
    private BigDecimal width;
    @Positive(message = "Length must be positive if provided")
    private BigDecimal length;
    @Positive(message = "Weight must be positive if provided")
    private BigDecimal weight;
    @PositiveOrZero(message = "Current price cannot be negative if provided")
    private Integer currentPrice;
}
