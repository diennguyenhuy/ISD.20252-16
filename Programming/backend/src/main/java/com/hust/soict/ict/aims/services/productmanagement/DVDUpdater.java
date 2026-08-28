package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateDVDRequest;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

@Component
class DVDUpdater extends ProductUpdater<DVD, UpdateDVDRequest> {

    DVDUpdater() {
        super(UpdateDVDRequest.class);
    }

    @Override
    protected void updateProduct(DVD product, UpdateDVDRequest request) {
        request.getGenre().ifDefined(product::setGenre);
        request.getDirector().ifDefined(product::setDirector);
        request.getRuntime().ifDefined(product::setRuntime);
        request.getStudio().ifDefined(product::setStudio);
        request.getLanguage().ifDefined(product::setLanguage);
        request.getSubtitles().ifDefined(product::setSubtitles);
    }
}
