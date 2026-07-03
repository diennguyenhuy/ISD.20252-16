package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

interface ProductCommonCreator<C extends CreateProductRequest, B extends Product.Builder<?, B>> {
    default B buildCommonFields(B builder, C request) {
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
