package com.hust.soict.ict.aims.models.entities.product;

import java.util.*;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "newspaper")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Newspaper extends PrintableProduct {
    @Column(nullable = false)
    private String editorInChief;

    @Immutable
    @Column(length = 25, updatable = false)
    private String issueNumber;

    @Column(length = 25)
    private String publicationFrequency;

    @Immutable
    @Column(length = 9, updatable = false)
    private String ISSN;

    @ElementCollection
    @CollectionTable(
            name = "newspaper_sections",
            joinColumns = @JoinColumn(name = "newspaper_id")
    )
    @Column(name = "section", length = 50)
    private List<String> sections = new ArrayList<>();

    public List<String> getSections() {
        return Collections.unmodifiableList(sections);
    }

    private Newspaper(Builder builder) {
        super(builder);
        this.editorInChief = Objects.requireNonNull(builder.editorInChief, "Newspaper editor in chief cannot be null");
        this.issueNumber = builder.issueNumber;
        this.publicationFrequency = builder.publicationFrequency;
        this.ISSN = builder.ISSN;
        this.sections = builder.sections == null ? List.of() : List.copyOf(builder.sections);
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    private void apply(Builder builder) {
        super.apply(builder);
        Optional.ofNullable(builder.editorInChief).ifPresent(v -> this.editorInChief = v);
        Optional.ofNullable(builder.publicationFrequency).ifPresent(v -> this.publicationFrequency = v);
        Optional.ofNullable(builder.sections).ifPresent(v -> this.sections = List.copyOf(v));
    }

    public static class Builder extends PrintableProduct.Builder<Newspaper, Builder> {
        private String editorInChief;
        private String issueNumber;
        private String publicationFrequency;
        private String ISSN;
        private List<String> sections;

        public Builder() {
            super();
        }

        public Builder(Newspaper updatingNewspaper) {
            super(updatingNewspaper);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Newspaper build() {
            if (updatingProduct != null) {
                updatingProduct.apply(this);
                return updatingProduct;
            } else return new Newspaper(this);
        }

        public Builder editorInChief(String editorInChief) {
            this.editorInChief = editorInChief;
            return this;
        }

        public Builder issueNumber(String issueNumber) {
            this.issueNumber = issueNumber;
            return this;
        }

        public Builder publicationFrequency(String publicationFrequency) {
            this.publicationFrequency = publicationFrequency;
            return this;
        }

        public Builder ISSN(String ISSN) {
            this.ISSN = ISSN;
            return this;
        }

        public Builder sections(Collection<String> sections) {
            this.sections = List.copyOf(sections);
            return this;
        }

        public Builder sections(String... sections) {
            this.sections = List.of(sections);
            return this;
        }
    }
}
