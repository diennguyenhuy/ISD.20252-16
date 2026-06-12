package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateCDRequest extends CreateProductRequest {
    @Past(message = "Release date must be in the past compared to now")
    private LocalDate releaseDate;
    @NotBlank(message = "Genre cannot be blank")
    private String genre;
    @NotEmpty(message = "Artists cannot be empty")
    private List<@NotBlank(message = "Each artist cannot be blank") String> artists;
    @NotBlank(message = "Record label cannot be blank")
    private String recordLabel;
    @NotEmpty(message = "Tracks cannot be empty")
    private List<@Valid TrackRequest> tracks;
}
