package com.hust.soict.ict.aims.models.entities.product;

import com.hust.soict.ict.aims.exceptions.ProductConstructionException;
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

    /// Units: minutes min
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

    private DVD(Builder builder) {
        super(builder);

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
        public DVD build() throws ProductConstructionException {
            this.validate().throwProductConstructionExceptionIfAny();
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

        @Override
        protected Builder validate() throws ProductConstructionException {
            super.validate();
            this.validate(() -> requireNotNull(this.discType, "discType"));
            this.validate(() -> requireNonBlank(this.director, "director"));
            this.validate(() -> requirePositive(this.runtime, "runtime"));
            this.validate(() -> requireNonBlank(this.studio, "studio"));
            this.validate(() -> requireNonBlank(this.language, "language"));
            this.validate(() -> requireNotEmpty(this.subtitles, "subtitles"));

            return this;
        }
    }
}
