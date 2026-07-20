package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class UpdatePrintableProductRequest extends UpdateProductRequest {
    private UpdateFieldRequest<@NotBlank(message = "Publisher must not be blank if provided") String>
            publisher = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NullOrNotBlank(message = "Language must not be blank if provided") String>
            language = UpdateFieldRequest.undefined();
}
