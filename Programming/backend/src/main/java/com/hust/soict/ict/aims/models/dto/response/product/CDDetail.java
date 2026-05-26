package com.hust.soict.ict.aims.models.dto.response.product;

import com.hust.soict.ict.aims.models.entities.product.CD;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CDDetail extends ProductDetail {
    private LocalDate releaseDate;
    private String genre;
    private List<String> artists;
    private String recordLabel;
    private List<TrackDetail> tracks;

    public CDDetail() {
        super(CD.class.getSimpleName());
    }
}
