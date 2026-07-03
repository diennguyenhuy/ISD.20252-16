package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdatePrintableProductRequest;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

interface PrintableProductCommonUpdater<U extends UpdatePrintableProductRequest, B extends PrintableProduct.Builder<?, B>> extends ProductCommonUpdater<U, B> {
    @Override
    default B buildCommonFields(B builder, U request) {
        return ProductCommonUpdater.super.buildCommonFields(builder, request)
                .publisher(request.getPublisher())
                .language(request.getLanguage());
    }
}
