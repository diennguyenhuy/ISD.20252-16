package com.hust.soict.ict.aims.models.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateNewspaperRequest extends UpdateProductRequest {
    private String publisher;
    private LocalDate publicationDate;
    private String language;
    private String editorInChief;
    private String issueNumber;
    private String publicationFrequency;
    private String ISSN;
    private List<String> sections;
}
