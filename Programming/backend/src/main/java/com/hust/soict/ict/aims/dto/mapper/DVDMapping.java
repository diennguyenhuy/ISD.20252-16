package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.DVDDetail;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DVDMapping implements ProductMapping<DVD, DVDDetail> {
    @Override
    public Class<DVD> getProductClass() {
        return DVD.class;
    }

    @Override
    public List<String> mapCreators(DVD product) {
        return List.of(product.getStudio());
    }

    @Override
    public DVDDetail newProductDetail() {
        return new DVDDetail();
    }

    @Override
    public void map(DVDDetail productDetail, DVD product) {
        productDetail.setReleaseDate(product.getReleaseDate());
        productDetail.setGenre(product.getGenre());
        productDetail.setDiscType(product.getDiscType().name());
        productDetail.setDirector(product.getDirector());
        productDetail.setRuntime(product.getRuntime());
        productDetail.setStudio(product.getStudio());
        productDetail.setLanguage(product.getLanguage());
        productDetail.setSubtitles(product.getSubtitles());
    }
}
