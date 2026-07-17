package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.PrintableProductDetail;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

import java.util.function.Supplier;

abstract class PrintableProductMapper<P extends PrintableProduct, D extends PrintableProductDetail> extends ProductMapper<P, D> {

    protected PrintableProductMapper(Supplier<D> responseSupplier) {
        super(responseSupplier);
    }

    @Override
    public D map(P source) {
        D productDetail = super.map(source);

        productDetail.setPublisher(source.getPublisher());
        productDetail.setPublicationDate(source.getPublicationDate());
        productDetail.setLanguage(source.getLanguage());

        return productDetail;
    }
}
