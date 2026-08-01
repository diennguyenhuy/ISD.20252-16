package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "cd")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CD extends Product {
    @Immutable
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

    @ElementCollection
    @CollectionTable(
            name = "cd_tracks",
            joinColumns = @JoinColumn(name = "cd_id")
    )
    @OrderColumn(name = "track_number")
    private List<Track> tracks = new ArrayList<>();

    public List<String> getArtists() {
        return Collections.unmodifiableList(artists);
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    private CD(Builder builder) {
        super(builder);
        this.releaseDate = builder.releaseDate;
        this.genre = Objects.requireNonNull(builder.genre, "CD genre cannot be null");
        this.artists = new ArrayList<>(Objects.requireNonNull(builder.artists, "List of artists cannot be null"));
        this.recordLabel = Objects.requireNonNull(builder.recordLabel, "CD record label cannot be null");
        this.tracks = new ArrayList<>(Objects.requireNonNull(builder.tracks, "List of tracks cannot be null"));
    }

    public static class Builder extends Product.Builder<Builder> {
        private LocalDate releaseDate;
        private String genre;
        private List<String> artists;
        private String recordLabel;
        private List<Track> tracks;

        public Builder() {
            super();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public CD build() {
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

        public Builder artists(Collection<String> artists) {
            this.artists = artists == null ? null : List.copyOf(artists);
            return this;
        }

        public Builder artists(String... artists) {
            this.artists = artists == null ? null : List.of(artists);
            return this;
        }

        public Builder recordLabel(String recordLabel) {
            this.recordLabel = recordLabel;
            return this;
        }

        public Builder tracks(Collection<Track> tracks) {
            this.tracks = tracks == null ? null : List.copyOf(tracks);
            return this;
        }

        public Builder tracks(Track... tracks) {
            this.tracks = tracks == null ? null : List.of(tracks);
            return this;
        }
    }

    static {
        registerUpdateCommand(UpdateCommand.Genre.class, CD.class, (p, c) -> p.genre = c.newValue());
        registerUpdateCommand(UpdateCommand.Artists.class, CD.class, (p, c) -> p.artists = new ArrayList<>(c.newValue()));
        registerUpdateCommand(UpdateCommand.RecordLabel.class, CD.class, (p, c) -> p.recordLabel = c.newValue());
        registerUpdateCommand(UpdateCommand.Tracks.class, CD.class, (p, c) -> p.tracks = new ArrayList<>(c.newValue()));
    }

    public interface UpdateCommand<T> extends Product.UpdateCommand<T> {
        record Genre(String newValue) implements UpdateCommand<String> {}
        record Artists(List<String> newValue) implements UpdateCommand<List<String>> {}
        record RecordLabel(String newValue) implements UpdateCommand<String> {}
        record Tracks(List<Track> newValue) implements UpdateCommand<List<Track>> {}
    }
}
