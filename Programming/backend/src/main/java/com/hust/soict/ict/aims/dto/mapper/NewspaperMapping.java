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
    public NewspaperDetail map(Newspaper product) {
        if (product == null) {
            return null;
        }

        NewspaperDetail detail = new NewspaperDetail();
        mapCommonFields(detail, product);
        detail.setEditorInChief(product.getEditorInChief());
        detail.setIssueNumber(product.getIssueNumber());
        detail.setPublicationFrequency(product.getPublicationFrequency());
        detail.setISSN(product.getISSN());
        detail.setSections(product.getSections());

        return detail;
    }

    @Override
    public List<String> mapCreators(Newspaper product) {
        return List.of(product.getPublisher());
    }
}
