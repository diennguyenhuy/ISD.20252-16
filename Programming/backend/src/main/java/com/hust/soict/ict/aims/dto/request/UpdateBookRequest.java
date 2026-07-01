package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import com.hust.soict.ict.aims.constraints.NullOrNotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateBookRequest extends UpdatePrintableProductRequest {
    @NullOrNotEmpty(message = "Authors must not be empty if provided")
    private List<@NotBlank(message = "Each author must not be blank if provided") String> authors;
    @Positive(message = "Number of pages must be positive if provided")
    private Integer numberOfPages;
    @NullOrNotBlank(message = "Genre must not be blank if provided")
    private String genre;
}
