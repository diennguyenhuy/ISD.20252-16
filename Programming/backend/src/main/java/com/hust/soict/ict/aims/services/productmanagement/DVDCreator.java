package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateDVDRequest;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

@Component
class DVDCreator extends ProductCreator<DVD, CreateDVDRequest, DVD.Builder> {

    DVDCreator() {
        super(DVD.Builder::new);
    }

    @Override
    public Class<CreateDVDRequest> createRequestType() {
        return CreateDVDRequest.class;
    }

    @Override
    public DVD createFrom(CreateDVDRequest createRequest) {
        return builder(createRequest)
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
