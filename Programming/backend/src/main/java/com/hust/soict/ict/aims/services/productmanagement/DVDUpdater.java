package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateDVDRequest;
import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

@Component
class DVDUpdater implements ProductUpdater<DVD, UpdateDVDRequest>, ProductCommonUpdater<UpdateDVDRequest, DVD.Builder> {
    @Override
    public Class<UpdateDVDRequest> updateRequestType() {
        return UpdateDVDRequest.class;
    }

    @Override
    public DVD updateFrom(DVD existingProduct, UpdateDVDRequest updateRequest) {
        return buildCommonFields(existingProduct.toBuilder(), updateRequest)
                .genre(updateRequest.getGenre())
                .director(updateRequest.getDirector())
                .runtime(updateRequest.getRuntime())
                .studio(updateRequest.getStudio())
                .language(updateRequest.getLanguage())
                .subtitles(updateRequest.getSubtitles())
                .build();
    }
}
