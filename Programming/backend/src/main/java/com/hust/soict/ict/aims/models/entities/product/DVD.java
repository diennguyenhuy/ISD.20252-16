package com.hust.soict.ict.aims.models.entities.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dvd")
@Getter @Setter
@NoArgsConstructor
public class DVD extends Product {
    private LocalDate releaseDate;

    @Column(length = 50)
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private DiscType discType;

    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    private Duration runtime;

    @Column(nullable = false)
    private String studio;

    @Column(nullable = false, length = 50)
    private String language;

    @ElementCollection
    @CollectionTable(name = "dvd_subtitles", joinColumns = @JoinColumn(name = "dvd_id"))
    @Column(name = "subtitle", columnDefinition = "TEXT")
    private List<String> subtitles = new ArrayList<>();
}
