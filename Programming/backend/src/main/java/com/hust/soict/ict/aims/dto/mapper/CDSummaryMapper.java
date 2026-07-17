package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.models.entities.product.CD;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CDSummaryMapper extends ProductSummaryMapper<CD> {
    @Override
    protected List<String> mapCreators(CD product) {
        return product.getArtists();
    }

    @Override
    public Class<CD> getSourceClass() {
        return CD.class;
    }
}
