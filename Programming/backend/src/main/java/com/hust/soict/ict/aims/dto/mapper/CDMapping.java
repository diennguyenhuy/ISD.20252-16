package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.CDDetail;
import com.hust.soict.ict.aims.dto.response.product.TrackDetail;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CDMapping implements ProductMapping<CD, CDDetail> {
    @Override
    public Class<CD> getProductClass() {
        return CD.class;
    }

    @Override
    public CDDetail map(CD product) {
        if (product == null) {
            return null;
        }
        CDDetail detail = new CDDetail();
        mapCommonFields(detail, product);
        detail.setReleaseDate(product.getReleaseDate());
        detail.setGenre(product.getGenre());
        detail.setArtists(product.getArtists());
        detail.setRecordLabel(product.getRecordLabel());
        detail.setTracks(product.getTracks().stream().map(this::map).toList());

        return detail;
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
}
