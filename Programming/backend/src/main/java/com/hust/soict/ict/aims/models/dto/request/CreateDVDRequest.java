package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateDVDRequest extends CreateProductRequest {
    @Past(message = "Release date must be in the past compared to now")
    private LocalDate releaseDate;
    private String genre;

    @NotNull(message = "Disc type is required for DVD")
    private String discType;
    @NotBlank(message = "Director cannot be blank")
    private String director;
    @NotNull
    @Positive(message = "Runtime must be positive")
    private Integer runtime;
    @NotBlank(message = "Studio cannot be blank")
    private String studio;
    @NotBlank(message = "Language cannot be blank")
    private String language;
    @NotEmpty(message = "Subtitles cannot be blank")
    private List<String> subtitles;
}