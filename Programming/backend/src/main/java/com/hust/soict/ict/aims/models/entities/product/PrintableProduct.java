package com.hust.soict.ict.aims.models.entities.product;

import java.time.LocalDate;

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

    @Column(nullable = false)
    private LocalDate publicationDate;

    @Column(length = 50)
    private String language;

    protected PrintableProduct(Builder<? extends Builder<?>> builder) {
        super(builder);

        this.publisher = builder.publisher;
        this.publicationDate = builder.publicationDate;
        this.language = builder.language;
    }

    public static abstract class Builder<B extends Builder<B>> extends Product.Builder<B> {
        private String publisher;
        private LocalDate publicationDate;
        private String language;

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

        @Override
        protected B validate() {
            super.validate();
            this.validate(() -> requireNonBlank(this.publisher, "publisher"));
            this.validate(() -> requireNotNull(this.publicationDate, "publicationDate"));

            return self();
        }
    }
}
