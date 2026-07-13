package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.NewspaperDetail;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class NewspaperMapping implements PrintableProductMapping<Newspaper, NewspaperDetail> {

    @Override
    public Class<Newspaper> getProductClass() {
        return Newspaper.class;
    }

    @Override
    public void map(NewspaperDetail productDetail, Newspaper product) {
        productDetail.setEditorInChief(product.getEditorInChief());
        productDetail.setIssueNumber(product.getIssueNumber());
        productDetail.setPublicationFrequency(product.getPublicationFrequency());
        productDetail.setISSN(product.getISSN());
        productDetail.setSections(product.getSections());
    }

    @Override
    public List<String> mapCreators(Newspaper product) {
        return List.of(product.getPublisher());
    }

    @Override
    public NewspaperDetail newProductDetail() {
        return new NewspaperDetail();
    }
}
