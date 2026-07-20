package com.hust.soict.ict.aims.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private UpdateFieldRequest<@NotBlank(message = "Title must not be blank if provided") String>
            title = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotBlank(message = "Category must not be blank if provided") String>
            category = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NullOrNotBlank(message = "Description must not be blank if provided") String>
            description = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NullOrNotBlank(message = "Image URL must not be blank if provided") String>
            imageURL = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotNull @Positive(message = "Height must be positive if provided") BigDecimal>
            height = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotNull @Positive(message = "Width must be positive if provided") BigDecimal>
            width = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotNull @Positive(message = "Length must be positive if provided") BigDecimal>
            length = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotNull @Positive(message = "Weight must be positive if provided") BigDecimal>
            weight = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NotNull @PositiveOrZero(message = "Current price cannot be negative if provided") Long>
            currentPrice = UpdateFieldRequest.undefined();
}
