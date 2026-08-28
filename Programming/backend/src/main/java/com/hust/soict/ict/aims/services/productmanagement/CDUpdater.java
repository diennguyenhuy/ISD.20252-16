package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateCDRequest;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

@Component
class CDUpdater extends ProductUpdater<CD, UpdateCDRequest> {

    CDUpdater() {
        super(UpdateCDRequest.class);
    }

    @Override
    protected void updateProduct(CD product, UpdateCDRequest request) {
        request.getGenre().ifDefined(product::setGenre);
        request.getArtists().ifDefined(product::setArtists);
        request.getRecordLabel().ifDefined(product::setRecordLabel);
        request.getTracks()
                .map(ts -> ts.stream()
                        .map(t -> new Track(t.title(), t.length()))
                        .toList()
                ).ifDefined(product::setTracks);
    }
}
