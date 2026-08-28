package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdatePrintableProductRequest;
import com.hust.soict.ict.aims.models.entities.product.PrintableProduct;

abstract class PrintableProductUpdater<P extends PrintableProduct, U extends UpdatePrintableProductRequest> extends ProductUpdater<P, U> {
    protected PrintableProductUpdater(Class<U> updateRequestType) {
        super(updateRequestType);
    }

    @Override
    protected void updateProduct(P product, U request) {
        request.getPublisher().ifDefined(product::setPublisher);
        request.getLanguage().ifDefined(product::setLanguage);
        updatePrintableProduct(product, request);
    }

    protected abstract void updatePrintableProduct(P product, U request);
}
