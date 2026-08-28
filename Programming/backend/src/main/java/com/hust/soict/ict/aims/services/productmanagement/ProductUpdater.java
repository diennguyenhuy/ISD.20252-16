package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

abstract class ProductUpdater<P extends Product, U extends UpdateProductRequest> {
    final Class<U> updateRequestType;

    protected ProductUpdater(Class<U> updateRequestType) {
        this.updateRequestType = updateRequestType;
    }

    protected abstract void updateProduct(P product, U request);

    final P update(P product, U request) {
        request.getTitle().ifDefined(product::setTitle);
        request.getCategory().ifDefined(product::setCategory);
        request.getDescription().ifDefined(product::setDescription);
        request.getImageURL().ifDefined(product::setImageURL);
        request.getHeight().ifDefined(product::setHeight);
        request.getWidth().ifDefined(product::setWidth);
        request.getLength().ifDefined(product::setLength);
        request.getWeight().ifDefined(product::setWeight);
        updateProduct(product, request);

        return product;
    }
}
