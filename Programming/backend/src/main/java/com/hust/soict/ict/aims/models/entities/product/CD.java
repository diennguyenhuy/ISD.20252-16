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

    public void addTrack(@NonNull Track track) {
        track.setCd(this);
        tracks.add(track);
    }

    public Track getTrack(UUID trackId) {
        for (Track track : tracks) {
            if (track.getId().equals(trackId)) {
                return track;
            }
        }
        return null;
    }

    public void removeTrack(UUID trackId) {
        tracks.removeIf(track -> track.getId().equals(trackId));
    }

    private CD(Builder builder) {
        super(builder);
        this.releaseDate = builder.releaseDate;
        this.genre = Objects.requireNonNull(builder.genre, "CD genre cannot be null");
        this.artists = List.copyOf(Objects.requireNonNull(builder.artists, "List of artists cannot be null"));
        this.recordLabel = Objects.requireNonNull(builder.recordLabel, "CD record label cannot be null");
        this.tracks = List.copyOf(Objects.requireNonNull(builder.tracks, "List of tracks cannot be null"));
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    private void apply(Builder builder) {
        super.apply(builder);
        Optional.ofNullable(builder.genre).ifPresent(v -> this.genre = v);
        Optional.ofNullable(builder.artists).ifPresent(v -> this.artists = List.copyOf(v));
        Optional.ofNullable(builder.recordLabel).ifPresent(v -> this.recordLabel = v);
    }

    public static class Builder extends Product.Builder<CD, Builder> {
        private LocalDate releaseDate;
        private String genre;
        private List<String> artists;
        private String recordLabel;
        private List<Track> tracks;

        public Builder() {
            super();
        }

        private Builder(CD updatingCD) {
            super(updatingCD);
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

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder artists(Collection<String> artists) {
            this.artists = List.copyOf(artists);
            return this;
        }

        public Builder artists(String... artists) {
            this.artists = List.of(artists);
            return this;
        }

        public Builder recordLabel(String recordLabel) {
            this.recordLabel = recordLabel;
            return this;
        }

        public Builder tracks(Collection<Track> tracks) {
            this.tracks = List.copyOf(tracks);
            return this;
        }

        public Builder tracks(Track... tracks) {
            this.tracks = List.of(tracks);
            return this;
        }
    }
}
