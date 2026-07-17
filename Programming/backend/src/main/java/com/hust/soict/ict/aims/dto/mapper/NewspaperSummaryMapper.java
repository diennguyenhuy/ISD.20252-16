package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.models.entities.product.Newspaper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class NewspaperSummaryMapper extends ProductSummaryMapper<Newspaper> {
    @Override
    protected List<String> mapCreators(Newspaper product) {
        return List.of(product.getPublisher());
    }

    @Override
    public Class<Newspaper> getSourceClass() {
        return Newspaper.class;
    }
}
