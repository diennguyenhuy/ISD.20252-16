package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdatePrintableProductRequest;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

interface PrintableProductUpdater<P extends PrintableProduct, U extends UpdatePrintableProductRequest> extends ProductUpdater<P, U> {
    default <B extends PrintableProduct.Builder<P, B>> B buildCommonFields(B builder, U request) {
        return ProductUpdater.super.buildCommonFields(builder, request)
                .publisher(request.getPublisher())
                .language(request.getLanguage());
    }
}
