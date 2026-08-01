package com.hust.soict.ict.aims.dto.response.product;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewspaperDetail extends PrintableProductDetail {
    private String editorInChief;
    private String issueNumber;
    private String publicationFrequency;
    private String ISSN;
    private List<String> sections;
}
