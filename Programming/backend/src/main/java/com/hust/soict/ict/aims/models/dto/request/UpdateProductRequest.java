package com.hust.soict.ict.aims.models.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "productType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateBookRequest.class, name = "BOOK"),
        @JsonSubTypes.Type(value = UpdateCDRequest.class, name = "CD"),
        @JsonSubTypes.Type(value = UpdateDVDRequest.class, name = "DVD"),
        @JsonSubTypes.Type(value = UpdateNewspaperRequest.class, name = "NEWSPAPER")
})
public class UpdateProductRequest {
    private String productType;

    private String title;
    private String category;
    private String description;

    @Min(value = 0, message = "Current price must be positive")
    private Long currentPrice;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private String imageURL;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal weight;
}