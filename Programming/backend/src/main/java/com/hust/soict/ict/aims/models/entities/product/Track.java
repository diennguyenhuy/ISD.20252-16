package com.hust.soict.ict.aims.models.entities.product;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "track")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(updatable = false, insertable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    /**
     * Units: seconds s
     */
    @Column(nullable = false)
    private Integer length;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_id", nullable = false)
    private CD cd;

    void setCd(CD cd) {
        Objects.requireNonNull(cd, "CD is null");
        this.cd = cd;
    }

    public Track(String title, Integer length) throws ProductValidationException {
        TrackValidator.validateTitle(title);
        TrackValidator.validateLength(length);

        this.title = title;
        this.length = length;
    }
}

final class TrackValidator {
    private TrackValidator() {}

    static void validateTitle(String title) throws ProductValidationException {
        if (title == null || title.isBlank()) {
            throw new ProductValidationException("Title is required", "title");
        }
    }

    static void validateLength(Integer length) throws ProductValidationException {
        if (length == null) {
            throw new ProductValidationException("Length is required", "length");
        }

        if (length <= 0) {
            throw new ProductValidationException("Length must be positive", "length");
        }
    }
}
