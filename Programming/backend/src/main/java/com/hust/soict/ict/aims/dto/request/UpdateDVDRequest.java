package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.NullOrNotBlank;
import com.hust.soict.ict.aims.constraints.NullOrNotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateDVDRequest extends UpdateProductRequest {
    @NullOrNotBlank(message = "Genre must not be blank if provided")
    private String genre;
    @NullOrNotBlank(message = "Director must not be blank if provided")
    private String director;
    @Positive(message = "Runtime must be positive if provided")
    private Integer runtime;
    @NullOrNotBlank(message = "Studio must not be blank if provided")
    private String studio;
    @NullOrNotBlank(message = "Language must not be blank if provided")
    private String language;
    @NullOrNotEmpty(message = "Subtitles must not be empty if provided")
    private List<String> subtitles;
}
