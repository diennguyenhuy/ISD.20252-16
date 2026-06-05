package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "cd")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CD extends Product {
    @Column(updatable = false)
    private LocalDate releaseDate;

    @Column(nullable = false, length = 50)
    private String genre;

    @ElementCollection
    @CollectionTable(
            name = "cd_artists",
            joinColumns = @JoinColumn(name = "cd_id")
    )
    @Column(name = "artist")
    private List<String> artists = new ArrayList<>();

    @Column(nullable = false)
    private String recordLabel;

    @OneToMany(mappedBy = "cd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Track> tracks = new ArrayList<>();

    public List<String> getArtists() {
        return Collections.unmodifiableList(artists);
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void addTrack(@NonNull Track track) throws IllegalArgumentException {
        track.setCd(this);
        tracks.add(track);
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    private CD(Builder builder) {
        super(builder);
        this.releaseDate = builder.releaseDate;
        this.genre = Objects.requireNonNull(builder.genre, "CD genre cannot be null");
        this.artists = List.copyOf(builder.artists);
        this.recordLabel = builder.recordLabel;
        this.tracks = List.copyOf(builder.tracks);
    }

    private void apply(Builder builder) {
        super.apply(builder);
        this.genre = builder.genre;
        this.artists = List.copyOf(builder.artists);
        this.recordLabel = builder.recordLabel;
        builder.tracks.forEach(this::addTrack);
    }

    public static class Builder extends Product.Builder<CD, Builder> {
        private LocalDate releaseDate;
        private String genre;
        private List<String> artists = new ArrayList<>();
        private String recordLabel;
        private List<Track> tracks = new ArrayList<>();

        public Builder() {
            super();
        }

        private Builder(CD existingCD) {
            super(existingCD);
            this.releaseDate = existingCD.releaseDate;
            this.genre = existingCD.genre;
            this.artists = existingCD.artists;
            this.recordLabel = existingCD.recordLabel;
            this.tracks = existingCD.tracks;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public CD build() {
            if (updatingProduct != null) {
                updatingProduct.apply(this);
                return updatingProduct;
            } else return new CD(this);
        }

        public Builder releaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder genre(@NonNull String genre) {
            this.genre = genre;
            return this;
        }

        public Builder artist(@NonNull String artist) {
            this.artists.add(artist);
            return this;
        }

        public Builder artists(@NonNull Collection<String> artists) {
            this.artists = List.copyOf(artists);
            return this;
        }

        public Builder artists(@NonNull String... artists) {
            this.artists = List.of(artists);
            return this;
        }

        public Builder recordLabel(@NonNull String recordLabel) {
            this.recordLabel = recordLabel;
            return this;
        }

        public Builder track(@NonNull Track track) {
            this.tracks.add(track);
            return this;
        }

        public Builder tracks(@NonNull Collection<Track> tracks) {
            this.tracks = List.copyOf(tracks);
            return this;
        }

        public Builder tracks(@NonNull Track... tracks) {
            this.tracks = List.of(tracks);
            return this;
        }
    }
}
