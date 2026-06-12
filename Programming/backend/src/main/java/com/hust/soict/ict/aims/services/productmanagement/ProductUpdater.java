package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

public interface ProductUpdater<P extends Product, U extends UpdateProductRequest> {
    Class<U> updateRequestType();
    P updateFrom(P existingProduct, U updateRequest);

    default <B extends Product.Builder<P, B>> B buildCommonFields(B builder, U request) {
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
