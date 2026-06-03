package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateNewspaperRequest extends CreateProductRequest {
    @NotBlank(message = "Publisher must not be blank")
    private String publisher;
    @NotNull(message = "Publication date cannot be null")
    @Past(message = "Publication date must be in the past compared to now")
    private LocalDate publicationDate;
    private String language;
    @NotBlank(message = "Publisher must not be blank")
    private String editorInChief;
    private String issueNumber;
    private String publicationFrequency;
    private String ISSN;
    private List<String> sections;
}