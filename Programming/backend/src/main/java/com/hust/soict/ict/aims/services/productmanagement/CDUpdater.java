package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateCDRequest;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CDUpdater implements ProductUpdater<CD, UpdateCDRequest> {
    @Override
    public Class<UpdateCDRequest> updateRequestType() {
        return UpdateCDRequest.class;
    }

    @Override
    public CD updateFrom(CD existingProduct, UpdateCDRequest updateRequest) {
        List<Track> tracks = updateRequest.getTracks() == null ?
                null : updateRequest.getTracks().stream()
                .map(t -> new Track(t.getTitle(), t.getLength()))
                .toList();

        return buildCommonFields(existingProduct.toBuilder(), updateRequest)
                .genre(updateRequest.getGenre())
                .artists(updateRequest.getArtists())
                .recordLabel(updateRequest.getRecordLabel())
                .tracks(tracks)
                .build();
    }
}
