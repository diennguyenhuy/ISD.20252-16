package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class BookDetail extends ProductDetail {
    private String publisher;
    private LocalDate publicationDate;
    private String language;
    private List<String> authors;
    private String coverType;
    private int numberOfPages;
    private String genre;
}
