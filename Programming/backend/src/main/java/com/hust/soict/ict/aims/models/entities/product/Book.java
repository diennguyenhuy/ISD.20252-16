package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

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
    @Immutable
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

    public static class Builder extends PrintableProduct.Builder<Builder> {
        private List<String> authors;
        private CoverType coverType;
        private Integer numberOfPages;
        private String genre;

        public Builder() {
            super();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Book build() {
            return new Book(this);
        }

        public Builder authors(Collection<String> authors) {
            this.authors = authors == null ? null : List.copyOf(authors);
            return this;
        }

        public Builder authors(String... authors) {
            this.authors = authors == null ? null : List.of(authors);
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

    static {
        registerUpdateCommand(UpdateCommand.Authors.class, Book.class, (p, c) -> p.authors = new ArrayList<>(c.newValue()));
        registerUpdateCommand(UpdateCommand.NumberOfPages.class, Book.class, (p, c) -> p.numberOfPages = c.newValue());
        registerUpdateCommand(UpdateCommand.Genre.class, Book.class, (p, c) -> p.genre = c.newValue());
    }

    public interface UpdateCommand<T> extends PrintableProduct.UpdateCommand<T> {
        record Authors(List<String> newValue) implements UpdateCommand<List<String>> {}
        record NumberOfPages(Integer newValue) implements UpdateCommand<Integer> {}
        record Genre(String newValue) implements UpdateCommand<String> {}
    }
}
