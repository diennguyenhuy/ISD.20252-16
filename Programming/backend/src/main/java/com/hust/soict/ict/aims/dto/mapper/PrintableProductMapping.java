package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.PrintableProductDetail;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

interface PrintableProductMapping<P extends PrintableProduct, D extends PrintableProductDetail> extends ProductMapping<P, D> {
    @Override
    default D map(P product) {
        D productDetail = ProductMapping.super.map(product);
        productDetail.setPublisher(product.getPublisher());
        productDetail.setPublicationDate(product.getPublicationDate());
        productDetail.setLanguage(product.getLanguage());
        return productDetail;
    }
}
