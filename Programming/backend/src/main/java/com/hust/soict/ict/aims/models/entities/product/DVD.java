package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

    @Column(updatable = false)
    private LocalDate releaseDate;

    @Column(length = 50)
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 7)
    private DiscType discType;

    @Column(nullable = false)
    private String director;

    /// Units: minutes min
    @Column(nullable = false)
    private int runtime;

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
        this.discType = Objects.requireNonNull(builder.discType, "DVD Disc Type cannot be null");
        this.director = Objects.requireNonNull(builder.director, "DVD Director cannot be null");
        this.runtime = Objects.requireNonNull(builder.runtime, "DVD Runtime cannot be null");
        this.studio = Objects.requireNonNull(builder.studio, "DVD Studio cannot be null");
        this.language = Objects.requireNonNull(builder.language, "DVD Language cannot be null");
        this.subtitles = List.copyOf(builder.subtitles);
    }

    private void apply(Builder builder) {
        super.apply(builder);
        this.genre = builder.genre;
        this.director = builder.director;
        this.runtime = builder.runtime;
        this.studio = builder.studio;
        this.language = builder.language;
        this.subtitles = builder.subtitles;
    }

    public static class Builder extends Product.Builder<DVD, Builder> {
        private LocalDate releaseDate;
        private String genre;
        private DiscType discType;
        private String director;
        private Integer runtime;
        private String studio;
        private String language;
        private List<String> subtitles = new ArrayList<>();

        public Builder() {
            super();
        }

        public Builder(DVD existingDVD) {
            super(existingDVD);
            this.releaseDate = existingDVD.releaseDate;
            this.genre = existingDVD.genre;
            this.discType = existingDVD.discType;
            this.director = existingDVD.director;
            this.runtime = existingDVD.runtime;
            this.studio = existingDVD.studio;
            this.language = existingDVD.language;
            this.subtitles = existingDVD.subtitles;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public DVD build() {
            if (updatingProduct != null) {
                updatingProduct.apply(this);
                return updatingProduct;
            } else return new DVD(this);
        }

        public Builder releaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder discType(@NonNull DiscType discType) {
            this.discType = discType;
            return this;
        }

        public Builder discType(@NonNull String discType) {
            this.discType = DiscType.valueOf(discType.toUpperCase());
            return this;
        }

        public Builder director(@NonNull String director) {
            this.director = director;
            return this;
        }

        public Builder runtime(int runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder studio(@NonNull String studio) {
            this.studio = studio;
            return this;
        }

        public Builder language(@NonNull String language) {
            this.language = language;
            return this;
        }

        public Builder subtitle(@NonNull String subtitle) {
            this.subtitles.add(subtitle);
            return this;
        }

        public Builder subtitles(@NonNull Collection<String> subtitles) {
            this.subtitles.addAll(subtitles);
            return this;
        }

        public Builder subtitles(@NonNull String... subtitles) {
            Collections.addAll(this.subtitles, subtitles);
            return this;
        }
    }
}
