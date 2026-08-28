package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "dvd")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DVD extends Product {
    public enum DiscType {
        BLU_RAY,
        HD_DVD,
    }

    @Immutable
    @Column(updatable = false)
    private LocalDate releaseDate;

    @Column(length = 50)
    @Setter
    private String genre;

    @Enumerated(EnumType.STRING)
    @Immutable
    @Column(nullable = false, updatable = false, length = 7)
    private DiscType discType;

    @Column(nullable = false)
    @Setter
    private String director;

    /// Units: minutes min
    @Column(nullable = false)
    @Setter
    private int runtime;

    @Column(nullable = false)
    @Setter
    private String studio;

    @Column(nullable = false, length = 50)
    @Setter
    private String language;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "subtitles", nullable = false)
    @Setter
    private List<String> subtitles = new ArrayList<>();

    public List<String> getSubtitles() {
        return Collections.unmodifiableList(subtitles);
    }

    private DVD(Builder builder) {
        super(builder);
        this.releaseDate = builder.releaseDate;
        this.genre = builder.genre;
        this.discType = Objects.requireNonNull(builder.discType, "DVD Disc Type cannot be null");
        this.director = Objects.requireNonNull(builder.director, "DVD Director cannot be null");
        this.runtime = Objects.requireNonNull(builder.runtime, "DVD Runtime cannot be null");
        this.studio = Objects.requireNonNull(builder.studio, "DVD Studio cannot be null");
        this.language = Objects.requireNonNull(builder.language, "DVD Language cannot be null");
        this.subtitles = new ArrayList<>(Objects.requireNonNull(builder.subtitles, "DVD Subtitles cannot be null"));
    }

    public static class Builder extends Product.Builder<Builder> {
        private LocalDate releaseDate;
        private String genre;
        private DiscType discType;
        private String director;
        private Integer runtime;
        private String studio;
        private String language;
        private List<String> subtitles;

        public Builder() {
            super();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public DVD build() {
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

        public Builder discType(String discType) {
            this.discType = DiscType.valueOf(discType.toUpperCase());
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

        public Builder subtitles(Collection<String> subtitles) {
            this.subtitles = subtitles == null ? null : List.copyOf(subtitles);
            return this;
        }

        public Builder subtitles(String... subtitles) {
            this.subtitles = subtitles == null ? null : List.of(subtitles);
            return this;
        }
    }
}
