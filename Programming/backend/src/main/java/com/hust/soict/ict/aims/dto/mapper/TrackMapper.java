package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.TrackDetail;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

@Component
class TrackMapper extends AbstractMapper<Track, TrackDetail> {

    TrackMapper() {
        super(TrackDetail::new);
    }

    @Override
    public void map(Track source, TrackDetail target) {
        target.setTitle(source.getTitle());
        target.setLength(source.getLength());
    }

    @Override
    public Class<Track> getSourceClass() {
        return Track.class;
    }

    @Override
    public Class<TrackDetail> getTargetClass() {
        return TrackDetail.class;
    }
}
