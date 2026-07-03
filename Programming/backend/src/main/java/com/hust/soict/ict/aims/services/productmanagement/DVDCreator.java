package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateDVDRequest;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

@Component
class DVDCreator implements ProductCreator<DVD, CreateDVDRequest>, ProductCommonCreator<CreateDVDRequest, DVD.Builder> {
    @Override
    public Class<CreateDVDRequest> createRequestType() {
        return CreateDVDRequest.class;
    }

    @Override
    public DVD createFrom(CreateDVDRequest createRequest) {
        return buildCommonFields(new DVD.Builder(), createRequest)
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
