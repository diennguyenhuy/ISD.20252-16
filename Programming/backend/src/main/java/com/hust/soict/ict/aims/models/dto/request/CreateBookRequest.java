package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateBookRequest extends CreateProductRequest {
    @NotBlank(message = "Publisher is required for books")
    private String publisher;
    private LocalDate publicationDate;
    private String language;
    private List<String> authors;
    private String coverType;

    @Min(value = 1, message = "A book must have at least 1 page")
    private int numberOfPages;
    private String genre;
}