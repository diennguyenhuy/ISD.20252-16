package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.NewspaperDetail;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

@Component
class NewspaperMapper extends PrintableProductMapper<Newspaper, NewspaperDetail> {

    NewspaperMapper() {
        super(NewspaperDetail::new);
    }

    @Override
    public void map(Newspaper source, NewspaperDetail target) {
        target.setEditorInChief(source.getEditorInChief());
        target.setIssueNumber(source.getIssueNumber());
        target.setPublicationFrequency(source.getPublicationFrequency());
        target.setISSN(source.getISSN());
        target.setSections(source.getSections());
    }

    @Override
    public Class<Newspaper> getSourceClass() {
        return Newspaper.class;
    }
}
