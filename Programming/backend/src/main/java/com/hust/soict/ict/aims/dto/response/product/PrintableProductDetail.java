package com.hust.soict.ict.aims.dto.response.product;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class PrintableProductDetail extends ProductDetail {
    private String publisher;
    private LocalDate publicationDate;
    private String language;
}
