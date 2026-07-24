package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.List;


abstract class ProductSummaryMapper<P extends Product> extends AbstractMapper<P, ProductSummary> {
    protected ProductSummaryMapper() {
        super(ProductSummary::new);
    }

    protected abstract List<String> mapCreators(P product);

    @Override
    public final Class<ProductSummary> getTargetClass() {
        return ProductSummary.class;
    }

    @Override
    public void map(P source, ProductSummary target) {
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setOriginalValue(source.getOriginalValue());
        target.setCurrentPrice(source.getCurrentPrice());
        target.setStockQuantity(source.getStockQuantity());
        target.setProductType(getSourceClass().getSimpleName());
        target.setCreators(mapCreators(source));
        target.setStatus(source.getStatus().name());
        target.setImageURL(source.getImageURL());
    }
}
