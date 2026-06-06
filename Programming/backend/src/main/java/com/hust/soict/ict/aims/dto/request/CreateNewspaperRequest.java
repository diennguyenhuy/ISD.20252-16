package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import com.hust.soict.ict.aims.constraints.NullOrNotEmpty;
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
    @NullOrNotBlank(message = "Language must not be blank if provided")
    private String language;
    @NotBlank(message = "Publisher must not be blank")
    private String editorInChief;
    @NullOrNotBlank(message = "Issue number must not be blank if provided")
    private String issueNumber;
    @NullOrNotBlank(message = "Publication frequency must not be blank if provided")
    private String publicationFrequency;
    @NullOrNotBlank(message = "ISSN must not be blank if provided")
    private String ISSN;
    @NullOrNotEmpty(message = "Sections must not be empty if provided")
    private List<String> sections;
}
