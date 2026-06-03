package com.hust.soict.ict.aims.models.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
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
