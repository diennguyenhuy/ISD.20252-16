package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateBookRequest extends CreatePrintableProductRequest {
    @NotEmpty(message = "Authors cannot be empty")
    private List<@NotBlank(message = "Each author must not be blank") String> authors;
    @NotBlank(message = "Cover type cannot be null")
    private String coverType;
    @Positive(message = "A book must have at least 1 page")
    private Integer numberOfPages;
    @NullOrNotBlank(message = "Genre must not be blank if provided")
    private String genre;
}
