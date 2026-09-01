package com.hust.soict.ict.aims.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> sections;
}
