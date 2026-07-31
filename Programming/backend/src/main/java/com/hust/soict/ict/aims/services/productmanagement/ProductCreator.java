package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.function.Supplier;

abstract class ProductCreator<
        P extends Product,
        C extends CreateProductRequest,
        B extends Product.Builder<B>
        > {
    private final Supplier<B> builderSupplier;
    private final Class<C> createRequestType;

    protected ProductCreator(Class<C> createRequestType, Supplier<B> builderSupplier) {
        this.createRequestType = createRequestType;
        this.builderSupplier = builderSupplier;
    }

    final Class<C> createRequestType() {
        return createRequestType;
    }
    protected abstract P createFrom(C createRequest);

    protected B builder(C request) {
        return builderSupplier.get()
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
