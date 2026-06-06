package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import com.hust.soict.ict.aims.constraints.NullOrNotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateBookRequest extends UpdateProductRequest {
    @NullOrNotBlank(message = "Publisher must not be blank if provided")
    private String publisher;
    @NullOrNotBlank(message = "Language must not be blank if provided")
    private String language;
    @NullOrNotEmpty(message = "Authors must not be empty if provided")
    private List<String> authors;
    @Positive(message = "Number of pages must be positive if provided")
    private Integer numberOfPages;
    @NullOrNotBlank(message = "Genre must not be blank if provided")
    private String genre;
}
