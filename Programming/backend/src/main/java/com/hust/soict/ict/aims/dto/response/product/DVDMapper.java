package com.hust.soict.ict.aims.dto.response.product;

import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DVDMapper extends ProductMapper<DVD, DVDDetail> {
    DVDMapper() {
        super(DVDDetail::new, DVD.class);
    }

    @Override
    protected void mapProduct(DVD product, DVDDetail productDetail) {
        productDetail.setReleaseDate(product.getReleaseDate());
        productDetail.setGenre(product.getGenre());
        productDetail.setDiscType(product.getDiscType().name());
        productDetail.setDirector(product.getDirector());
        productDetail.setRuntime(product.getRuntime());
        productDetail.setStudio(product.getStudio());
        productDetail.setLanguage(product.getLanguage());
        productDetail.setSubtitles(product.getSubtitles());
    }

    @Override
    protected List<String> creators(DVD product) {
        return List.of(product.getStudio(), product.getDirector());
    }
}
