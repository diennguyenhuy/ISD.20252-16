package com.hust.soict.ict.aims.models.entities.product;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private Integer numberOfPages;

    @Column(length = 50)
    private String genre;

    public List<String> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public Book(Builder builder) throws ProductValidationException {
        super(builder);
        BookValidator.validateAuthors(builder.authors);
        BookValidator.validateCoverType(builder.coverType);

        this.authors = new ArrayList<>(builder.authors);
        this.numberOfPages = builder.numberOfPages;
        this.genre = builder.genre;
        this.coverType = builder.coverType;
    }

    public static class Builder extends PrintableProduct.Builder<Builder> {
        private List<String> authors = new ArrayList<>();
        private CoverType coverType;
        private int numberOfPages;
        private String genre;

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Book build() throws ProductValidationException {
            return new Book(this);
        }

        public Builder authors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public Builder coverType(CoverType coverType) {
            this.coverType = coverType;
            return this;
        }

        public Builder numberOfPages(Integer numberOfPages) {
            this.numberOfPages = numberOfPages;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }
    }
}

final class BookValidator {
    private BookValidator() {}

    static void validateAuthors(List<String> authors) throws ProductValidationException {
        if (authors == null || authors.isEmpty()) {
            throw new ProductValidationException("Authors are required", "authors");
        }

        for (String author : authors) {
            if (author == null || author.isBlank()) {
                throw new ProductValidationException("Some authors in list are null or blank", "authors");
            }
        }
    }

    static void validateCoverType(CoverType coverType) throws ProductValidationException {
        if (coverType == null) {
            throw new ProductValidationException("Cover type is required", "coverType");
        }
    }
}
