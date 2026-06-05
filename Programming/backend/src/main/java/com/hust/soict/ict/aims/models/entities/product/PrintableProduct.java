package com.hust.soict.ict.aims.models.entities.product;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PrintableProduct extends Product {
    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false, updatable = false)
    private LocalDate publicationDate;

    @Column(length = 50)
    private String language;

    protected PrintableProduct(Builder<?, ?> builder) {
        super(builder);
        this.publisher = Objects.requireNonNull(builder.publisher, "Printable Product publisher cannot be null.");
        this.publicationDate = Objects.requireNonNull(builder.publicationDate, "Printable Product publication date cannot be null.");
        this.language = builder.language;
    }

    protected final void apply(Builder<?, ?> builder) {
        super.apply(builder);
        this.publisher = builder.publisher;
        this.language = builder.language;
    }

    public static abstract class Builder<P extends PrintableProduct, B extends Builder<P, B>> extends Product.Builder<P, B> {
        private String publisher;
        private LocalDate publicationDate;
        private String language;

        protected Builder() {
            super();
        }

        protected Builder(PrintableProduct existingProduct) {
            super(existingProduct);
            this.publisher = existingProduct.publisher;
            this.publicationDate = existingProduct.publicationDate;
            this.language = existingProduct.language;
        }

        public B publisher(@NonNull String publisher) {
            this.publisher = publisher;
            return self();
        }

        public B publicationDate(@NonNull LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return self();
        }

        public B language(String language) {
            this.language = language;
            return self();
        }
    }
}
