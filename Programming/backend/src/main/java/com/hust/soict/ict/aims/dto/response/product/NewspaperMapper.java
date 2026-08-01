package com.hust.soict.ict.aims.dto.response.product;

import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class NewspaperMapper extends PrintableProductMapper<Newspaper, NewspaperDetail> {
    NewspaperMapper() {
        super(NewspaperDetail::new, Newspaper.class);
    }

    @Override
    protected void mapPrintableProduct(Newspaper product, NewspaperDetail productDetail) {
        productDetail.setEditorInChief(product.getEditorInChief());
        productDetail.setIssueNumber(product.getIssueNumber());
        productDetail.setPublicationFrequency(product.getPublicationFrequency());
        productDetail.setISSN(product.getISSN());
        productDetail.setSections(product.getSections());
    }

    @Override
    protected List<String> creators(Newspaper product) {
        return List.of(product.getPublisher(), product.getEditorInChief());
    }
}
