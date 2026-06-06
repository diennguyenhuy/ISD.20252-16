package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateCDRequest;
import com.hust.soict.ict.aims.models.entities.product.CD;
import org.springframework.stereotype.Component;

@Component
public class CDUpdater implements ProductUpdater<CD, UpdateCDRequest> {
    @Override
    public Class<UpdateCDRequest> updateRequestType() {
        return UpdateCDRequest.class;
    }

    @Override
    public CD updateFrom(CD existingProduct, UpdateCDRequest updateRequest) {
        return populateCommonFields(existingProduct.toBuilder(), updateRequest)
                .genre(updateRequest.getGenre())
                .artists(updateRequest.getArtists())
                .recordLabel(updateRequest.getRecordLabel())
                .build();
    }
}
