package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.models.dto.request.CreateCDRequest;
import com.hust.soict.ict.aims.models.entities.product.CD;
import com.hust.soict.ict.aims.models.entities.product.Track;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CDCreator implements ProductCreator<CD, CreateCDRequest> {
    @Override
    public Class<CreateCDRequest> createRequestType() {
        return CreateCDRequest.class;
    }

    @Override
    public CD createFrom(CreateCDRequest createRequest) {
        List<Track> cdTracks = createRequest.getTracks() == null ? List.of() : createRequest.getTracks().stream()
                .map(t -> new Track(t.getTitle(), t.getLength())).collect(Collectors.toList());

        return populateCommonFields(new CD.Builder(), createRequest)
                .releaseDate(createRequest.getReleaseDate())
                .genre(createRequest.getGenre())
                .artists(createRequest.getArtists())
                .recordLabel(createRequest.getRecordLabel())
                .tracks(cdTracks)
                .build();
    }
}
