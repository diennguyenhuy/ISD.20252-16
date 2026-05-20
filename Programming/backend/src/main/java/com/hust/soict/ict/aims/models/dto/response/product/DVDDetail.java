package com.hust.soict.ict.aims.models.dto.response.product;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class DVDDetail extends ProductDetail {
    private LocalDate releaseDate;
    private String genre;
    private String discType;
    private String director;
    private int runtime;
    private String studio;
    private String language;
    private List<String> subtitles;
}
