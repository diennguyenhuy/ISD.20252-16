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
    private String title;

    /// Units: seconds s
    @Column(nullable = false)
    private Integer length;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_id", nullable = false)
    private CD cd;

    public void setCd(@NonNull CD cd) {
        this.cd = cd;
    }

    public Track(String title, int length) {
        this.title = title;
        this.length = length;
    }
}
