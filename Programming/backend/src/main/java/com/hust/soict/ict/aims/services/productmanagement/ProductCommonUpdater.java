package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

interface ProductCommonUpdater<U extends UpdateProductRequest, B extends Product.Builder<?, B>> {
    default B buildCommonFields(B builder, U request) {
        return builder
                .title(request.getTitle())
                .category(request.getCategory())
                .description(request.getDescription())
                .height(request.getHeight())
                .width(request.getWidth())
                .length(request.getLength())
                .weight(request.getWeight())
                .imageURL(request.getImageURL());
    }
}
