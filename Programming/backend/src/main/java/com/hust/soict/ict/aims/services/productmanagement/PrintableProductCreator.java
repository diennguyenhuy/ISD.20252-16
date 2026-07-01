package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreatePrintableProductRequest;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

interface PrintableProductCreator<P extends PrintableProduct, C extends CreatePrintableProductRequest> extends ProductCreator<P, C> {
    default <B extends PrintableProduct.Builder<P, B>> B buildCommonFields(B builder, C request) {
        return ProductCreator.super.buildCommonFields(builder, request)
                .publisher(request.getPublisher())
                .publicationDate(request.getPublicationDate())
                .language(request.getLanguage());
    }
}
