package com.hust.soict.ict.aims.models.entities.product;

import java.util.*;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "newspaper")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Newspaper extends PrintableProduct {
    @Column(nullable = false)
    @Setter
    private String editorInChief;

    @Immutable
    @Column(length = 25, updatable = false)
    private String issueNumber;

    @Column(length = 25)
    @Setter
    private String publicationFrequency;

    @Immutable
    @Column(length = 9, updatable = false)
    private String ISSN;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "sections")
    @Setter
    private List<String> sections;

    public Optional<List<String>> getSections() {
        return Optional.ofNullable(sections == null ? null : Collections.unmodifiableList(sections));
    }

    public Optional<String> getISSN() {
        return Optional.ofNullable(ISSN);
    }

    public Optional<String> getIssueNumber() {
        return Optional.ofNullable(issueNumber);
    }

    public Optional<String> getPublicationFrequency() {
        return Optional.ofNullable(publicationFrequency);
    }

    private Newspaper(Builder builder) {
        super(builder);
        this.editorInChief = Objects.requireNonNull(builder.editorInChief, "Newspaper editor in chief cannot be null");
        this.issueNumber = builder.issueNumber;
        this.publicationFrequency = builder.publicationFrequency;
        this.ISSN = builder.ISSN;
        this.sections = builder.sections == null ? new ArrayList<>() : new ArrayList<>(builder.sections);
    }

    public static class Builder extends PrintableProduct.Builder<Builder> {
        private String editorInChief;
        private String issueNumber;
        private String publicationFrequency;
        private String ISSN;
        private List<String> sections;

        public Builder() {
            super();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Newspaper build() {
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

        public Builder sections(Collection<String> sections) {
            this.sections = sections == null ? null : List.copyOf(sections);
            return this;
        }

        public Builder sections(String... sections) {
            this.sections = sections == null ? null : List.of(sections);
            return this;
        }
    }
}
