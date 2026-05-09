package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
public class NewspaperDetail extends ProductDetail {
    private String publisher;
    private LocalDate publicationDate;
    private String language;
    private String editorInChief;
    private String issueNumber;
    private String publicationFrequency;
    private String ISSN;
    private List<String> sections = new ArrayList<>();

}
