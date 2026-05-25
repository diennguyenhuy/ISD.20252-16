package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
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
