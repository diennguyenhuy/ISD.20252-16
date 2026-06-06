package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "track")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, insertable = false)
    private UUID id;

    @Column(nullable = false)
    @NonNull @Setter
    private String title;

    /// Units: seconds s
    @Column(nullable = false)
    @Setter
    private int length;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_id", nullable = false)
    @NonNull @Setter(AccessLevel.PACKAGE)
    private CD cd;

    public Track(@NonNull String title, int length) {
        this.title = title;
        this.length = length;
    }
}
