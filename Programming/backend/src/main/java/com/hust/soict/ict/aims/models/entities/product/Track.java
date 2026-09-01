package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Track {
    @Column(nullable = false)
    private String title;

    /// Units: seconds s
    @Column(nullable = false)
    private int length;

    @Override
    public int hashCode() {
        return Objects.hash(title, length);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Track that) {
            return length == that.length && Objects.equals(title, that.title);
        }
        return false;
    }

    public Track(@NonNull String title, int length) {
        this.title = title;
        this.length = length;
    }

    @Override
    public String toString() {
        return "Track(title=" + title + ", length=" + length + ")";
    }
}
