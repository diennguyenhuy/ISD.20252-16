package com.hust.soict.ict.aims.models.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdateDVDRequest extends UpdateProductRequest {
    private LocalDate releaseDate;
    private String genre;
    private String discType;
    private String director;
    private Integer runtime;
    private String studio;
    private String language;
    private List<String> subtitles;
}
