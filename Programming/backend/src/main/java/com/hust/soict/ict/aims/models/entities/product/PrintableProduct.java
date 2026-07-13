package com.hust.soict.ict.aims.models.entities.product;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        Optional.ofNullable(builder.publisher).ifPresent(v -> this.publisher = v);
        Optional.ofNullable(builder.publisher).ifPresent(v -> this.language = v);
    }

    public static abstract class Builder<P extends PrintableProduct, B extends Builder<P, B>> extends Product.Builder<P, B> {
        private String publisher;
        private LocalDate publicationDate;
        private String language;

        protected Builder() {
            super();
        }

        protected Builder(P updatingProduct) {
            super(updatingProduct);
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

    static {
        registerUpdateCommand(PrintableProductUpdateCommand.Publisher.class, PrintableProduct.class, (p, c) -> p.publisher = c.newValue());
        registerUpdateCommand(PrintableProductUpdateCommand.Language.class, PrintableProduct.class, (p, c) -> p.language = c.newValue());
    }
}
