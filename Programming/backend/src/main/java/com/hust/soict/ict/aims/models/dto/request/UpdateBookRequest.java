package com.hust.soict.ict.aims.models.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdateBookRequest extends UpdateProductRequest {
    private String publisher;
    private LocalDate publicationDate;
    private String language;
    private List<String> authors;
    private String coverType;
    private Integer numberOfPages;
    private String genre;
}