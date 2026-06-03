package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateDVDRequest;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

@Component
public class DVDCreator implements ProductCreator<DVD, CreateDVDRequest> {
    @Override
    public Class<CreateDVDRequest> createRequestType() {
        return CreateDVDRequest.class;
    }

    @Override
    public DVD createFrom(CreateDVDRequest createRequest) {
        return populateCommonFields(new DVD.Builder(), createRequest)
                .releaseDate(createRequest.getReleaseDate())
                .genre(createRequest.getGenre())
                .discType(createRequest.getDiscType())
                .director(createRequest.getDirector())
                .runtime(createRequest.getRuntime())
                .studio(createRequest.getStudio())
                .language(createRequest.getLanguage())
                .subtitles(createRequest.getSubtitles())
                .build();
    }
}
