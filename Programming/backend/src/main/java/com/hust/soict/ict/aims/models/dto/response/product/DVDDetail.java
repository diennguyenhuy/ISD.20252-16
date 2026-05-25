package com.hust.soict.ict.aims.models.dto.response.product;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
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
