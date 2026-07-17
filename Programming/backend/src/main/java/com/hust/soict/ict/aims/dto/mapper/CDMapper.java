package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.CDDetail;
import com.hust.soict.ict.aims.models.entities.product.CD;
import org.springframework.stereotype.Component;

@Component
class CDMapper extends ProductMapper<CD, CDDetail> {
    private final TrackMapper trackMapper;

    CDMapper(TrackMapper trackMapper) {
        super(CDDetail::new);
        this.trackMapper = trackMapper;
    }

    @Override
    public void map(CD source, CDDetail target) {
        target.setReleaseDate(source.getReleaseDate());
        target.setGenre(source.getGenre());
        target.setArtists(source.getArtists());
        target.setRecordLabel(source.getRecordLabel());
        target.setTracks(source.getTracks().stream().map(trackMapper::map).toList());
    }

    @Override
    public Class<CD> getSourceClass() {
        return CD.class;
    }
}
