package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateNewspaperRequest;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

@Component
public class NewspaperCreator implements ProductCreator<Newspaper, CreateNewspaperRequest> {
    @Override
    public Class<CreateNewspaperRequest> createRequestType() {
        return CreateNewspaperRequest.class;
    }

    @Override
    public Newspaper createFrom(CreateNewspaperRequest createRequest) {
        return buildCommonFields(new Newspaper.Builder(), createRequest)
                .publisher(createRequest.getPublisher())
                .publicationDate(createRequest.getPublicationDate())
                .language(createRequest.getLanguage())
                .editorInChief(createRequest.getEditorInChief())
                .issueNumber(createRequest.getIssueNumber())
                .publicationFrequency(createRequest.getPublicationFrequency())
                .ISSN(createRequest.getISSN())
                .sections(createRequest.getSections())
                .build();
    }
}
