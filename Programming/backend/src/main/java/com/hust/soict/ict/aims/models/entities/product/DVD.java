package com.hust.soict.ict.aims.models.entities.product;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "dvd")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DVD extends Product {
    private LocalDate releaseDate;

    @Column(length = 50)
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private DiscType discType;

    @Column(nullable = false)
    private String director;

    /**
     * Units: minutes min
     */
    @Column(nullable = false)
    private Integer runtime;

    @Column(nullable = false)
    private String studio;

    @Column(nullable = false, length = 50)
    private String language;

    @ElementCollection
    @CollectionTable(name = "dvd_subtitles", joinColumns = @JoinColumn(name = "dvd_id"))
    @Column(name = "subtitle", columnDefinition = "TEXT")
    private List<String> subtitles = new ArrayList<>();

    public List<String> getSubtitles() {
        return Collections.unmodifiableList(subtitles);
    }

    public DVD(Builder builder) throws ProductValidationException {
        super(builder);
        DVDValidator.validateDiscType(builder.discType);
        DVDValidator.validateDirector(builder.director);
        DVDValidator.validateRuntime(builder.runtime);
        DVDValidator.validateStudio(builder.studio);
        DVDValidator.validateLanguage(builder.language);
        DVDValidator.validateSubtitles(builder.subtitles);

        this.releaseDate = builder.releaseDate;
        this.genre = builder.genre;
        this.discType = builder.discType;
        this.director = builder.director;
        this.runtime = builder.runtime;
        this.studio = builder.studio;
        this.language = builder.language;
        this.subtitles = new ArrayList<>(builder.subtitles);
    }

    public static class Builder extends Product.Builder<Builder> {
        private LocalDate releaseDate;
        private String genre;
        private DiscType discType;
        private String director;
        private Integer runtime;
        private String studio;
        private String language;
        private List<String> subtitles = new ArrayList<>();

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public DVD build() throws ProductValidationException {
            return new DVD(this);
        }

        public Builder releaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder discType(DiscType discType) {
            this.discType = discType;
            return this;
        }

        public Builder director(String director) {
            this.director = director;
            return this;
        }

        public Builder runtime(Integer runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder studio(String studio) {
            this.studio = studio;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder subtitles(List<String> subtitles) {
            this.subtitles = subtitles;
            return this;
        }
    }
}

final class DVDValidator {
    private DVDValidator() {}

    static void validateDiscType(DiscType discType) throws ProductValidationException {
        if (discType == null) {
            throw new ProductValidationException("Disc Type is required", "discType");
        }
    }

    static void validateDirector(String director) throws ProductValidationException {
        if (director == null || director.isBlank()) {
            throw new ProductValidationException("Director is required", "director");
        }
    }

    static void validateRuntime(Integer runtime) {
        if (runtime == null) {
            throw new ProductValidationException("Runtime is required", "runtime");
        }
        if (runtime < 0) {
            throw new ProductValidationException("Runtime must be positive", "runtime");
        }
    }

    static void validateStudio(String studio) throws ProductValidationException {
        if (studio == null || studio.isBlank()) {
            throw new ProductValidationException("Studio is required", "studio");
        }
    }

    static void validateLanguage(String language) throws ProductValidationException {
        if (language == null || language.isBlank()) {
            throw new ProductValidationException("Language is required", "language");
        }
    }

    static void validateSubtitles(List<String> subtitles) throws ProductValidationException {
        if (subtitles == null || subtitles.isEmpty()) {
            throw new ProductValidationException("Subtitles are required", "subtitles");
        }
    }
}