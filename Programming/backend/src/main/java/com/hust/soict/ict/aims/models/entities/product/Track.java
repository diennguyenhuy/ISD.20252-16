package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Track {
    @Column(nullable = false)
    private String title;

    /// Units: seconds s
    @Column(nullable = false)
    private int length;

    public Track(@NonNull String title, int length) {
        this.title = title;
        this.length = length;
    }

    @Override
    public String toString() {
        return "Track(title=" + title + ", length=" + length + ")";
    }
}
