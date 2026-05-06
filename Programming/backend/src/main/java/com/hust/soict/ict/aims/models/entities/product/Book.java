package com.hust.soict.ict.aims.models.entities.product;

import com.hust.soict.ict.aims.exceptions.ProductConstructionException;
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

    private Book(Builder builder) throws ProductConstructionException {
        super(builder);
        validateBuilder(builder);
        builder.throwProductConstructionExceptionIfAny();

        this.authors = new ArrayList<>(builder.authors);
        this.numberOfPages = builder.numberOfPages;
        this.genre = builder.genre;
        this.coverType = builder.coverType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends PrintableProduct.Builder<Builder> {
        private List<String> authors = new ArrayList<>();
        private CoverType coverType;
        private int numberOfPages;
        private String genre;

        private Builder() {}

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Book build() throws ProductConstructionException {
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

    private static void validateBuilder(Builder builder) {
        builder.validate(() -> requireNotEmpty(builder.authors, "authors"));
        builder.validate(() -> requireNotNull(builder.coverType, "coverType"));
    }
}
