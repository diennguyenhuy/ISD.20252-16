package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.DVDDetail;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

@Component
class DVDMapper extends ProductMapper<DVD, DVDDetail> {

    DVDMapper() {
        super(DVD.class, DVDDetail::new);
    }

    @Override
    protected void mapProduct(DVD source, DVDDetail productDetail) {
        productDetail.setReleaseDate(source.getReleaseDate());
        productDetail.setGenre(source.getGenre());
        productDetail.setDiscType(source.getDiscType().name());
        productDetail.setDirector(source.getDirector());
        productDetail.setRuntime(source.getRuntime());
        productDetail.setStudio(source.getStudio());
        productDetail.setLanguage(source.getLanguage());
        productDetail.setSubtitles(source.getSubtitles());
    }
}
