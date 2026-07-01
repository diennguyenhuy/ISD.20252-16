package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class UpdatePrintableProductRequest extends UpdateProductRequest {
    @NullOrNotBlank(message = "Publisher must not be blank if provided")
    private String publisher;
    @NullOrNotBlank(message = "Language must not be blank if provided")
    private String language;
}
