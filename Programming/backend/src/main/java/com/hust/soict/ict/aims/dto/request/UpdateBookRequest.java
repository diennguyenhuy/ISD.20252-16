package com.hust.soict.ict.aims.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateBookRequest extends UpdateProductRequest {
    private String publisher;
    private LocalDate publicationDate;
    private String language;
    private List<String> authors;
    private String coverType;
    private Integer numberOfPages;
    private String genre;
}