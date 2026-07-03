package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreatePrintableProductRequest;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

interface PrintableProductCommonCreator<C extends CreatePrintableProductRequest, B extends PrintableProduct.Builder<?, B>> extends ProductCommonCreator<C, B> {
    @Override
    default B buildCommonFields(B builder, C request) {
        return ProductCommonCreator.super.buildCommonFields(builder, request)
                .publisher(request.getPublisher())
                .publicationDate(request.getPublicationDate())
                .language(request.getLanguage());
    }
}
