package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateNewspaperRequest;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

@Component
class NewspaperCreator extends PrintableProductCreator<Newspaper, CreateNewspaperRequest, Newspaper.Builder> {

    NewspaperCreator() {
        super(Newspaper.Builder::new);
    }

    @Override
    public Class<CreateNewspaperRequest> createRequestType() {
        return CreateNewspaperRequest.class;
    }

    @Override
    public Newspaper createFrom(CreateNewspaperRequest createRequest) {
        return builder(createRequest)
                .editorInChief(createRequest.getEditorInChief())
                .issueNumber(createRequest.getIssueNumber())
                .publicationFrequency(createRequest.getPublicationFrequency())
                .ISSN(createRequest.getISSN())
                .sections(createRequest.getSections())
                .build();
    }
}
