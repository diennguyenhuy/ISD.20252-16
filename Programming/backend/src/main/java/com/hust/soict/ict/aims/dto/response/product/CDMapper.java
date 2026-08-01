package com.hust.soict.ict.aims.dto.response.product;

import com.hust.soict.ict.aims.models.entities.product.CD;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CDMapper extends ProductMapper<CD, CDDetail> {
    CDMapper() {
        super(CDDetail::new, CD.class);
    }

    @Override
    protected void mapProduct(CD product, CDDetail productDetail) {
        productDetail.setReleaseDate(product.getReleaseDate());
        productDetail.setGenre(product.getGenre());
        productDetail.setArtists(product.getArtists());
        productDetail.setRecordLabel(product.getRecordLabel());
        productDetail.setTracks(product.getTracks().stream().map(t -> new TrackDetail(t.getTitle(), t.getLength())).toList());

    }

    @Override
    protected List<String> creators(CD product) {
        return product.getArtists();
    }
}
