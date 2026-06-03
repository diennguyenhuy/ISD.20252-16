package com.hust.soict.ict.aims.models.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    private String productType;
    private String title;
    private String category;
    private String description;
    private String imageURL;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal weight;
    private Integer currentPrice;
}