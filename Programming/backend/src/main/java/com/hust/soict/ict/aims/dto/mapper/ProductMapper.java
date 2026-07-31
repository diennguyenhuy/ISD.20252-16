package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.function.Supplier;

abstract class ProductMapper<P extends Product, D extends ProductDetail> extends AbstractMapper<P, D> {
    @SuppressWarnings("unchecked")
    protected ProductMapper(Class<P> sourceClass, Supplier<D> responseSupplier) {
        super(sourceClass, (Class<D>) ProductDetail.class, responseSupplier);
    }

    @Override
    protected final void map(P source, D productDetail) {
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
        mapProduct(source, productDetail);
    }

    protected abstract void mapProduct(P source, D productDetail);
}
