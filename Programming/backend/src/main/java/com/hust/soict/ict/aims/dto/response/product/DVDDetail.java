package com.hust.soict.ict.aims.dto.response.product;


import com.hust.soict.ict.aims.models.entities.product.DVD;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
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

    public DVDDetail() {
        super(DVD.class.getSimpleName());
    }
}
