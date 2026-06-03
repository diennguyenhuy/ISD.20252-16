package com.hust.soict.ict.aims.models.entities.product;

import java.util.*;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "newspaper")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Newspaper extends PrintableProduct {
    @Column(nullable = false)
    private String editorInChief;

    @Column(length = 25, updatable = false)
    private String issueNumber;

    @Column(length = 25)
    private String publicationFrequency;

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
        this.sections = List.copyOf(builder.sections);
    }

    private void apply(Builder builder) {
        super.apply(builder);
        this.editorInChief = builder.editorInChief;
        this.publicationFrequency = builder.publicationFrequency;
        this.sections = List.copyOf(builder.sections);
    }

    public static class Builder extends PrintableProduct.Builder<Newspaper, Builder> {
        private String editorInChief;
        private String issueNumber;
        private String publicationFrequency;
        private String ISSN;
        private List<String> sections = new ArrayList<>();

        public Builder() {
            super();
        }

        public Builder(Newspaper existingNewspaper) {
            super(existingNewspaper);
            this.editorInChief = existingNewspaper.editorInChief;
            this.issueNumber = existingNewspaper.issueNumber;
            this.publicationFrequency = existingNewspaper.publicationFrequency;
            this.ISSN = existingNewspaper.ISSN;
            this.sections = existingNewspaper.sections;
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

        public Builder editorInChief(@NonNull String editorInChief) {
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

        public Builder section(String section) {
            this.sections.add(section);
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
