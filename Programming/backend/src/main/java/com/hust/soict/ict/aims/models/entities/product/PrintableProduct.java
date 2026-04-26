package com.hust.soict.ict.aims.models.entities.product;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor
public abstract class PrintableProduct extends Product {
    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @Column(length = 50)
    private String language;
}
