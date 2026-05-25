package com.hust.soict.ict.aims.models.dto.response.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class CDDetail extends ProductDetail {
    private LocalDate releaseDate;
    private String genre;
    private List<String> artists;
    private String recordLabel;
    private List<TrackDetail> tracks;

}
