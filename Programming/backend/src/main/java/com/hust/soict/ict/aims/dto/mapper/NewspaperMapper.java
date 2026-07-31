package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.NewspaperDetail;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

@Component
class NewspaperMapper extends PrintableProductMapper<Newspaper, NewspaperDetail> {

    NewspaperMapper() {
        super(Newspaper.class, NewspaperDetail::new);
    }

    @Override
    protected void mapPrintableProduct(Newspaper source, NewspaperDetail productDetail) {
        productDetail.setEditorInChief(source.getEditorInChief());
        productDetail.setIssueNumber(source.getIssueNumber());
        productDetail.setPublicationFrequency(source.getPublicationFrequency());
        productDetail.setISSN(source.getISSN());
        productDetail.setSections(source.getSections());
    }
}
