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
        product.getIssueNumber().ifPresent(productDetail::setIssueNumber);
        product.getPublicationFrequency().ifPresent(productDetail::setPublicationFrequency);
        product.getISSN().ifPresent(productDetail::setISSN);
        product.getSections().ifPresent(productDetail::setSections);
    }

    @Override
    protected List<String> creators(Newspaper product) {
        return List.of("Publisher: " + product.getPublisher(), "Editor-In-Chief: " + product.getEditorInChief());
    }
}
