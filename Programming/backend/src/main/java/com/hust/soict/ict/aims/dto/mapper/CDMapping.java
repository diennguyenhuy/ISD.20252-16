package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.CDDetail;
import com.hust.soict.ict.aims.dto.response.product.TrackDetail;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CDMapping implements ProductMapping<CD, CDDetail> {
    @Override
    public Class<CD> getProductClass() {
        return CD.class;
    }

    public TrackDetail map(Track track) {
        if (track == null) {
            return null;
        }

        TrackDetail detail = new TrackDetail();
        detail.setTitle(track.getTitle());
        detail.setLength(track.getLength());

        return detail;
    }

    @Override
    public List<String> mapCreators(CD product) {
        return product.getArtists();
    }

    @Override
    public CDDetail newProductDetail() {
        return new CDDetail();
    }

    @Override
    public void map(CDDetail productDetail, CD product) {
        productDetail.setReleaseDate(product.getReleaseDate());
        productDetail.setGenre(product.getGenre());
        productDetail.setArtists(product.getArtists());
        productDetail.setRecordLabel(product.getRecordLabel());
        productDetail.setTracks(product.getTracks().stream().map(this::map).toList());
    }
}
