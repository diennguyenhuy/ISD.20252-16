package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateBookRequest extends CreateProductRequest {
    @NotBlank(message = "Publisher is required for books")
    private String publisher;
    @NotNull(message = "Publication date cannot be null")
    @Past(message = "Publication date must be in the past compared to now")
    private LocalDate publicationDate;
    private String language;
    @NotEmpty(message = "Authors cannot be empty")
    private List<String> authors;
    @NotNull(message = "Cover type cannot be null")
    private String coverType;

    @Positive(message = "A book must have at least 1 page")
    private Integer numberOfPages;
    private String genre;
}