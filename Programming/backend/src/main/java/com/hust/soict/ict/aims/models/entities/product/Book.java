package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;

@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends PrintableProduct {
    public enum CoverType {
        PAPERBACK,
        HARDCOVER,
    }

    @ElementCollection
    @CollectionTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id")
    )
    @Column(name = "author")
    private List<String> authors = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 9)
    private CoverType coverType;

    private Integer numberOfPages;

    @Column(length = 50)
    private String genre;

    public List<String> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    private Book(Builder builder) {
        super(builder);
        this.authors = List.copyOf(builder.authors);
        this.coverType = Objects.requireNonNull(builder.coverType, "Book cover type cannot be null");
        this.numberOfPages = builder.numberOfPages;
        this.genre = builder.genre;
    }

    private void apply(Builder builder) {
        super.apply(builder);
        this.authors = List.copyOf(builder.authors);
        this.numberOfPages = builder.numberOfPages;
        this.genre = builder.genre;
    }

    public static class Builder extends PrintableProduct.Builder<Book, Builder> {
        private List<String> authors = new ArrayList<>();
        private CoverType coverType;
        private Integer numberOfPages;
        private String genre;

        public Builder() {
            super();
        }

        public Builder(Book existingBook) {
            super(existingBook);
            this.authors = existingBook.authors;
            this.coverType = existingBook.coverType;
            this.numberOfPages = existingBook.numberOfPages;
            this.genre = existingBook.genre;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Book build() {
            if (updatingProduct != null) {
                updatingProduct.apply(this);
                return updatingProduct;
            } else return new Book(this);
        }

        public Builder author(@NonNull String author) {
            this.authors.add(author);
            return this;
        }

        public Builder authors(@NonNull Collection<String> authors) {
            this.authors = List.copyOf(authors);
            return this;
        }

        public Builder authors(@NonNull String... authors) {
            this.authors = List.of(authors);
            return this;
        }

        public Builder coverType(@NonNull CoverType coverType) {
            this.coverType = coverType;
            return this;
        }

        public Builder coverType(@NonNull String coverType) {
            this.coverType = CoverType.valueOf(coverType.toUpperCase());
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
