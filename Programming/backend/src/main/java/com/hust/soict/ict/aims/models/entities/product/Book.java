package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        this.authors = new ArrayList<>(Objects.requireNonNull(builder.authors, "List of authors cannot be null"));
        this.coverType = Objects.requireNonNull(builder.coverType, "Book cover type cannot be null");
        this.numberOfPages = builder.numberOfPages;
        this.genre = builder.genre;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    private void apply(Builder builder) {
        super.apply(builder);
        Optional.ofNullable(builder.authors).ifPresent(v -> this.authors = new ArrayList<>(v));
        Optional.ofNullable(builder.numberOfPages).ifPresent(v -> this.numberOfPages = v);
        Optional.ofNullable(builder.genre).ifPresent(v -> this.genre = v);
    }

    public static class Builder extends PrintableProduct.Builder<Book, Builder> {
        private List<String> authors;
        private CoverType coverType;
        private Integer numberOfPages;
        private String genre;

        public Builder() {
            super();
        }

        private Builder(Book updatingBook) {
            super(updatingBook);
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

        public Builder authors(Collection<String> authors) {
            this.authors = List.copyOf(authors);
            return this;
        }

        public Builder authors(String... authors) {
            this.authors = List.of(authors);
            return this;
        }

        public Builder coverType(CoverType coverType) {
            this.coverType = coverType;
            return this;
        }

        public Builder coverType(String coverType) {
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
