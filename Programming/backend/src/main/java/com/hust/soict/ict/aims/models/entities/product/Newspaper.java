package com.hust.soict.ict.aims.models.entities.product;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "newspaper")
@Getter @Setter
@NoArgsConstructor
public class Newspaper extends PrintableProduct {
    @Column(nullable = false)
    private String editorInChief;

    @Column(nullable = false, length = 25)
    private String issueNumber;

    @Column(length = 25)
    private String publicationFrequency;

    @Column(length = 9)
    private String ISSN;

    @ElementCollection
    @CollectionTable(
            name = "newspaper_sections",
            joinColumns = @JoinColumn(name = "newspaper_id")
    )
    @Column(name = "section", length = 50)
    private List<String> sections = new ArrayList<>();
}
