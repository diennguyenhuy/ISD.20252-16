package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CDDetail extends ProductDetail {
    private LocalDate releaseDate;
    private String genre;
    private List<String> artists;
    private String recordLabel;
    private List<TrackDetail> tracks;

}
