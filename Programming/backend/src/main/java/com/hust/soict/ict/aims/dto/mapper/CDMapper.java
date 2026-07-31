package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.CDDetail;
import com.hust.soict.ict.aims.dto.response.product.TrackDetail;
import com.hust.soict.ict.aims.models.entities.product.CD;
import org.springframework.stereotype.Component;

@Component
class CDMapper extends ProductMapper<CD, CDDetail> {
    CDMapper() {
        super(CD.class, CDDetail::new);
    }

    @Override
    protected void mapProduct(CD source, CDDetail productDetail) {
        productDetail.setReleaseDate(source.getReleaseDate());
        productDetail.setGenre(source.getGenre());
        productDetail.setArtists(source.getArtists());
        productDetail.setRecordLabel(source.getRecordLabel());
        productDetail.setTracks(source.getTracks().stream().map(t -> new TrackDetail(t.getTitle(), t.getLength())).toList());
    }
}
