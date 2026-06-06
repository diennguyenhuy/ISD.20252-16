package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

public interface ProductCreator<P extends Product, C extends CreateProductRequest> {
    Class<C> createRequestType();
    P createFrom(C createRequest);

    default <B extends Product.Builder<P, B>> B populateCommonFields(B builder, C request) {
        return builder
                .title(request.getTitle())
                .category(request.getCategory())
                .description(request.getDescription())
                .height(request.getHeight())
                .width(request.getWidth())
                .length(request.getLength())
                .weight(request.getWeight())
                .barcode(request.getBarcode())
                .originalValue(request.getOriginalValue())
                .currentPrice(request.getCurrentPrice())
                .stockQuantity(request.getStockQuantity())
                .imageURL(request.getImageURL());
    }
}
