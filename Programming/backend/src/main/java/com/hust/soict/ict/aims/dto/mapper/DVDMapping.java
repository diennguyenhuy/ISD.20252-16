package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.DVDDetail;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DVDMapping implements ProductMapping<DVD, DVDDetail> {
    @Override
    public Class<DVD> getProductClass() {
        return DVD.class;
    }

    @Override
    public DVDDetail map(DVD product) {
        if (product == null) {
            return null;
        }

        DVDDetail detail = new DVDDetail();
        mapCommonFields(detail, product);
        detail.setReleaseDate(product.getReleaseDate());
        detail.setGenre(product.getGenre());
        detail.setDiscType(product.getDiscType().name());
        detail.setDirector(product.getDirector());
        detail.setRuntime(product.getRuntime());
        detail.setStudio(product.getStudio());
        detail.setLanguage(product.getLanguage());
        detail.setSubtitles(product.getSubtitles());

        return detail;
    }

    @Override
    public List<String> mapCreators(DVD product) {
        return List.of(product.getStudio());
    }
}
