package com.hust.soict.ict.aims.models.dto.request;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "productType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateBookRequest.class, name = "BOOK"),
        @JsonSubTypes.Type(value = CreateCDRequest.class, name = "CD"),
        @JsonSubTypes.Type(value = CreateDVDRequest.class, name = "DVD"),
        @JsonSubTypes.Type(value = CreateNewspaperRequest.class, name = "NEWSPAPER")
})
public abstract class CreateProductRequest {
    @NotBlank(message = "Product type is required (BOOK, CD, DVD, NEWSPAPER)")
    private String productType;

    @NotBlank(message = "Title is required")
    private String title;

    private String category;
    private String description;

    @NotBlank(message = "Barcode is required")
    private String barcode;

    @Min(value = 0, message = "Original value must be positive")
    private long originalValue;

    @Min(value = 0, message = "Current price must be positive")
    private long currentPrice;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    private String imageURL;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal weight;
}