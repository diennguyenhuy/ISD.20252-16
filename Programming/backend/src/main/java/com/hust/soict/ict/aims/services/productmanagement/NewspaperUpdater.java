package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateNewspaperRequest;
import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

@Component
class NewspaperUpdater extends PrintableProductUpdater<Newspaper, UpdateNewspaperRequest> {

    NewspaperUpdater() {
        super(UpdateNewspaperRequest.class);
    }

    @Override
    protected void updatePrintableProduct(Newspaper product, UpdateNewspaperRequest request) {
        request.getEditorInChief().ifDefined(product::setEditorInChief);
        request.getPublicationFrequency().ifDefined(product::setPublicationFrequency);
        request.getSections().ifDefined(product::setSections);
    }
}
