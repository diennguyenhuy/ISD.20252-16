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
@Table(name = "cd")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CD extends Product {
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

    public void addTrack(Track track) throws IllegalArgumentException {
        if (track == null) {
            throw new IllegalArgumentException("track is null");
        }

        track.setCd(this);
        tracks.add(track);
    }

    public CD(Builder builder) throws ProductValidationException {
        super(builder);
        CDValidator.validateGenre(builder.genre);
        CDValidator.validateArtists(builder.artists);
        CDValidator.validateRecordLabel(builder.recordLabel);
        CDValidator.validateTracks(builder.tracks);

        this.releaseDate = builder.releaseDate;
        this.genre = builder.genre;
        this.artists = new ArrayList<>(builder.artists);
        this.recordLabel = builder.recordLabel;
        this.tracks = new ArrayList<>(builder.tracks);
    }

    public static class Builder extends Product.Builder<Builder> {
        private LocalDate releaseDate;
        private String genre;
        private List<String> artists = new ArrayList<>();
        private String recordLabel;
        private List<Track> tracks = new ArrayList<>();

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public CD build() throws ProductValidationException {
            return new CD(this);
        }

        public Builder releaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder artists(List<String> artists) {
            this.artists = artists;
            return this;
        }

        public Builder recordLabel(String recordLabel) {
            this.recordLabel = recordLabel;
            return this;
        }

        public Builder tracks(List<Track> tracks) {
            this.tracks = tracks;
            return this;
        }
    }

}

final class CDValidator {
    private CDValidator() {}

    static void validateArtists(List<String> artists) throws ProductValidationException {
        if (artists == null || artists.isEmpty()) {
            throw new ProductValidationException("Artists are required", "artists");
        }

        for (String artist : artists) {
            if (artist == null || artist.isBlank()) {
                throw new ProductValidationException("Some artists are null or blank", "artist");
            }
        }
    }

    static void validateRecordLabel(String recordLabel) throws ProductValidationException {
        if (recordLabel == null || recordLabel.isEmpty()) {
            throw new ProductValidationException("Record label is required", "recordLabel");
        }
    }

    static void validateTracks(List<Track> tracks) throws ProductValidationException {
        if (tracks == null || tracks.isEmpty()) {
            throw new ProductValidationException("Tracks are required", "tracks");
        }
    }

    static void validateGenre(String genre) throws ProductValidationException {
        if (genre == null || genre.isEmpty()) {
            throw new ProductValidationException("Genre is required", "genre");
        }
    }
}
