package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
@Getter @Setter
@NoArgsConstructor
public class Book extends PrintableProduct {
    @ElementCollection
    @CollectionTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id")
    )
    @Column(name = "author")
    private List<String> authors = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 9)
    private CoverType coverType;

    private int numberOfPages;

    @Column(length = 50)
    private String genre;
}
