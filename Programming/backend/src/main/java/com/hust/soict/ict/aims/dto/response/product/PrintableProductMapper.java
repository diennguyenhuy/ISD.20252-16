package com.hust.soict.ict.aims.dto.response.product;

import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

import java.util.function.Supplier;

abstract class PrintableProductMapper<P extends PrintableProduct, D extends PrintableProductDetail> extends ProductMapper<P, D> {
    protected PrintableProductMapper(Supplier<D> detailSupplier, Class<P> productClass) {
        super(detailSupplier, productClass);
    }

    @Override
    protected final void mapProduct(P product, D productDetail) {
        productDetail.setPublisher(product.getPublisher());
        productDetail.setPublicationDate(product.getPublicationDate());
        productDetail.setLanguage(product.getLanguage());
        mapPrintableProduct(product, productDetail);
    }

    protected abstract void mapPrintableProduct(P product, D productDetail);
}
