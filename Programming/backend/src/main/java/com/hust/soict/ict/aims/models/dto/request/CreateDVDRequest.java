package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateDVDRequest extends CreateProductRequest {
    private LocalDate releaseDate;
    private String genre;

    @NotBlank(message = "Disc type is required for DVD")
    private String discType;
    private String director;
    private int runtime;
    private String studio;
    private String language;
    private List<String> subtitles;
}