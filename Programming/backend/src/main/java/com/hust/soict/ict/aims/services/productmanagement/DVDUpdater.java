package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.models.dto.request.UpdateDVDRequest;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

@Component
public class DVDUpdater implements ProductUpdater<DVD, UpdateDVDRequest> {
    @Override
    public Class<UpdateDVDRequest> updateRequestType() {
        return UpdateDVDRequest.class;
    }

    @Override
    public DVD updateFrom(DVD existingProduct, UpdateDVDRequest updateRequest) {
        return populateCommonFields(new DVD.Builder(existingProduct), updateRequest)
                .genre(updateRequest.getGenre())
                .director(updateRequest.getDirector())
                .runtime(updateRequest.getRuntime())
                .studio(updateRequest.getStudio())
                .language(updateRequest.getLanguage())
                .subtitles(updateRequest.getSubtitles())
                .build();
    }
}
