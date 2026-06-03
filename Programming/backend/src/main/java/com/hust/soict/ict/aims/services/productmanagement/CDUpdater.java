package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.models.dto.request.UpdateCDRequest;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CDUpdater implements ProductUpdater<CD, UpdateCDRequest> {
    @Override
    public Class<UpdateCDRequest> updateRequestType() {
        return UpdateCDRequest.class;
    }

    @Override
    public CD updateFrom(CD existingProduct, UpdateCDRequest updateRequest) {
        List< Track > cdTracks = updateRequest.getTracks() == null ? List.of() : updateRequest.getTracks().stream()
                .map(t -> new Track(t.getTitle(), t.getLength())).collect(Collectors.toList());
        
        return populateCommonFields(new CD.Builder(existingProduct), updateRequest)
                .genre(updateRequest.getGenre())
                .artists(updateRequest.getArtists())
                .recordLabel(updateRequest.getRecordLabel())
                .tracks(cdTracks)
                .build();
    }
}
