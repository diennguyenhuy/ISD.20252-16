package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.models.entities.product.DVD;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DVDSummaryMapper extends ProductSummaryMapper<DVD> {
    @Override
    protected List<String> mapCreators(DVD product) {
        return List.of(product.getStudio());
    }

    @Override
    public Class<DVD> getSourceClass() {
        return DVD.class;
    }
}
