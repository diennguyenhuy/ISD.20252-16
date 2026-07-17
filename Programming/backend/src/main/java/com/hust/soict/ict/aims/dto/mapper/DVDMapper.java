package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.DVDDetail;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

@Component
class DVDMapper extends ProductMapper<DVD, DVDDetail> {

    DVDMapper() {
        super(DVDDetail::new);
    }

    @Override
    public void map(DVD source, DVDDetail target) {
        target.setReleaseDate(source.getReleaseDate());
        target.setGenre(source.getGenre());
        target.setDiscType(source.getDiscType().name());
        target.setDirector(source.getDirector());
        target.setRuntime(source.getRuntime());
        target.setStudio(source.getStudio());
        target.setLanguage(source.getLanguage());
        target.setSubtitles(source.getSubtitles());
    }

    @Override
    public Class<DVD> getSourceClass() {
        return DVD.class;
    }
}
