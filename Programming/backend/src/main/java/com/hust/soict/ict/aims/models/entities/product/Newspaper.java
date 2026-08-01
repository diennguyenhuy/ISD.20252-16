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

    static {
        registerUpdateCommand(UpdateCommand.EditorInChief.class, Newspaper.class, (p, c) -> p.editorInChief = c.newValue());
        registerUpdateCommand(UpdateCommand.PublicationFrequency.class, Newspaper.class, (p, c) -> p.publicationFrequency = c.newValue());
        registerUpdateCommand(UpdateCommand.Sections.class, Newspaper.class, (p, c) -> p.sections = c.newValue() == null ? new ArrayList<>() : new ArrayList<>(c.newValue()));
    }

    public interface UpdateCommand<T> extends PrintableProduct.UpdateCommand<T> {
        record EditorInChief(String newValue) implements UpdateCommand<String> {}
        record PublicationFrequency(String newValue) implements UpdateCommand<String> {}
        record Sections(List<String> newValue) implements UpdateCommand<List<String>> {}
    }
}
