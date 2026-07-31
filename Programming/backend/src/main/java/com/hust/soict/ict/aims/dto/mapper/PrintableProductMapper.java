package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.PrintableProductDetail;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

import java.util.function.Supplier;

abstract class PrintableProductMapper<P extends PrintableProduct, D extends PrintableProductDetail> extends ProductMapper<P, D> {

    protected PrintableProductMapper(Class<P> sourceClass, Supplier<D> responseSupplier) {
        super(sourceClass, responseSupplier);
    }

    @Override
    protected final void mapProduct(P source, D productDetail) {
        productDetail.setPublisher(source.getPublisher());
        productDetail.setPublicationDate(source.getPublicationDate());
        productDetail.setLanguage(source.getLanguage());
        mapPrintableProduct(source, productDetail);
    }

    protected abstract void mapPrintableProduct(P source, D productDetail);
}
