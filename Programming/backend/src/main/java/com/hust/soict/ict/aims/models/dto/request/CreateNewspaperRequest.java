package com.hust.soict.ict.aims.models.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateNewspaperRequest extends CreateProductRequest {
    private String publisher;
    private LocalDate publicationDate;
    private String language;
    private String editorInChief;
    private String issueNumber;
    private String publicationFrequency;
    private String ISSN;
    private List<String> sections;
}