package com.hust.soict.ict.aims.models.entities.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hust.soict.ict.aims.exceptions.ProductConstructionException;
import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "newspaper")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Newspaper extends PrintableProduct {
    @Column(nullable = false)
    private String editorInChief;

    @Column(length = 25)
    private String issueNumber;

    @Column(length = 25)
    private String publicationFrequency;

    @Column(length = 9)
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

    private Newspaper(Builder builder) throws ProductValidationException {
        super(builder);
        validateBuilder(builder);
        builder.throwProductConstructionExceptionIfAny();

        this.editorInChief = builder.editorInChief;
        this.issueNumber = builder.issueNumber;
        this.publicationFrequency = builder.publicationFrequency;
        this.ISSN = builder.ISSN;
        this.sections = builder.sections == null ? new ArrayList<>() : new ArrayList<>(builder.sections);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends PrintableProduct.Builder<Builder> {
        private String editorInChief;
        private String issueNumber;
        private String publicationFrequency;
        private String ISSN;
        private List<String> sections = new ArrayList<>();

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Newspaper build() throws ProductConstructionException {
            return new Newspaper(this);
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

        public Builder sections(List<String> sections) {
            this.sections = sections;
            return this;
        }
    }

    private static void validateBuilder(Builder builder) {
        builder.validate(() -> requireNonBlank(builder.editorInChief, "editorInChief"));
    }
}
