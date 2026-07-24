package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.function.Supplier;

abstract class ProductMapper<P extends Product, D extends ProductDetail> extends AbstractMapper<P, D> {

    protected ProductMapper(Supplier<D> responseSupplier) {
        super(responseSupplier);
    }

    @Override
    public D map(P source) {
        D productDetail = super.map(source);
        if (productDetail == null) {
            return null;
        }

        productDetail.setId(source.getId());
        productDetail.setTitle(source.getTitle());
        productDetail.setCategory(source.getCategory());
        productDetail.setDescription(source.getDescription());
        productDetail.setHeight(source.getHeight());
        productDetail.setWidth(source.getWidth());
        productDetail.setLength(source.getLength());
        productDetail.setWeight(source.getWeight());
        productDetail.setBarcode(source.getBarcode());
        productDetail.setOriginalValue(source.getOriginalValue());
        productDetail.setCurrentPrice(source.getCurrentPrice());
        productDetail.setStockQuantity(source.getStockQuantity());
        productDetail.setStatus(source.getStatus().name());
        productDetail.setImageURL(source.getImageURL());
        productDetail.setCreatedAt(source.getCreatedAt());
        productDetail.setUpdatedAt(source.getUpdatedAt());
        productDetail.setProductType(getSourceClass().getSimpleName());

        return productDetail;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Class<D> getTargetClass() {
        return (Class<D>) ProductDetail.class;
    }
}
