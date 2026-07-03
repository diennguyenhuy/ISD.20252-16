package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateNewspaperRequest;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

@Component
class NewspaperUpdater implements ProductUpdater<Newspaper, UpdateNewspaperRequest>, PrintableProductCommonUpdater<UpdateNewspaperRequest, Newspaper.Builder> {

    @Override
    public Class<UpdateNewspaperRequest> updateRequestType() {
        return UpdateNewspaperRequest.class;
    }

    @Override
    public Newspaper updateFrom(Newspaper existingProduct, UpdateNewspaperRequest updateRequest) {
        return buildCommonFields(existingProduct.toBuilder(), updateRequest)
                .editorInChief(updateRequest.getEditorInChief())
                .publicationFrequency(updateRequest.getPublicationFrequency())
                .sections(updateRequest.getSections())
                .build();
    }
}
