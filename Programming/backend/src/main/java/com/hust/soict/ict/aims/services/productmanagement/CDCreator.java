package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateCDRequest;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CDCreator extends ProductCreator<CD, CreateCDRequest, CD.Builder> {

    CDCreator() {
        super(CreateCDRequest.class, CD.Builder::new);
    }

    @Override
    public CD createFrom(CreateCDRequest createRequest) {
        List<Track> cdTracks = createRequest.getTracks() == null ?
                List.of() : createRequest.getTracks().stream()
                .map(t -> new Track(t.title(), t.length()))
                .toList();

        return builder(createRequest)
                .releaseDate(createRequest.getReleaseDate())
                .genre(createRequest.getGenre())
                .artists(createRequest.getArtists())
                .recordLabel(createRequest.getRecordLabel())
                .tracks(cdTracks)
                .build();
    }
}
