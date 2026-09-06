package com.hust.soict.ict.aims.models.entities.product;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "printable_product")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PrintableProduct extends Product {
    @Column(nullable = false)
    @Setter
    private String publisher;

    @Immutable
    @Column(nullable = false, updatable = false)
    private LocalDate publicationDate;

    @Column(length = 50)
    @Setter
    private String language;

    public Optional<String> getLanguage() {
        return Optional.ofNullable(language);
    }

    protected PrintableProduct(Builder<?> builder) {
        super(builder);
        this.publisher = Objects.requireNonNull(builder.publisher, "Printable Product publisher cannot be null.");
        this.publicationDate = Objects.requireNonNull(builder.publicationDate, "Printable Product publication date cannot be null.");
        this.language = builder.language;
    }

    public static abstract class Builder<B extends Builder<B>> extends Product.Builder<B> {
        private String publisher;
        private LocalDate publicationDate;
        private String language;

        protected Builder() {
            super();
        }

        public B publisher(String publisher) {
            this.publisher = publisher;
            return self();
        }

        public B publicationDate(LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return self();
        }

        public B language(String language) {
            this.language = language;
            return self();
        }
    }
}
