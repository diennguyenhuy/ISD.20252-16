package com.hust.soict.ict.aims.models.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateCDRequest extends CreateProductRequest {
    private LocalDate releaseDate;
    private String genre;
    private List<String> artists;
    private String recordLabel;
    private List<TrackDTO> tracks;
}