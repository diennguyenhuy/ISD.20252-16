package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateBookRequest extends UpdatePrintableProductRequest {
    private UpdateFieldRequest<
                @NotEmpty(message = "Authors must not be empty if provided")
                        List<@NotBlank(message = "Each author must not be blank if provided") String>
                > authors = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@Positive(message = "Number of pages must be positive if provided") Integer>
            numberOfPages = UpdateFieldRequest.undefined();
    private UpdateFieldRequest<@NullOrNotBlank(message = "Genre must not be blank if provided") String>
            genre = UpdateFieldRequest.undefined();
}
