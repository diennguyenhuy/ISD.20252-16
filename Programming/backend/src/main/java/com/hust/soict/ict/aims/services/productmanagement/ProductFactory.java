package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class ProductFactory {
    private final Map<Class<? extends CreateProductRequest>, ProductCreator<?, ?, ?>> productCreators;
    private final Map<Class<? extends UpdateProductRequest>, ProductUpdater<?, ?, ?>> productUpdaters;

    public ProductFactory(
            List<ProductCreator<?, ?, ?>> productCreators,
            List<ProductUpdater<?, ?, ?>> productUpdaters
    ) {
        this.productCreators = productCreators.stream()
                .collect(Collectors.toMap(
                        p -> p.createRequestType,
                        Function.identity()
                ));
        this.productUpdaters = productUpdaters.stream()
                .collect(Collectors.toMap(
                        p -> p.updateRequestType,
                        Function.identity()
                ));
    }

    @SuppressWarnings("unchecked")
    <P extends Product, C extends CreateProductRequest, B extends Product.Builder<B>>
    P createProduct(C request) {
        ProductCreator<P, C, B> creator = (ProductCreator<P, C, B>) productCreators.get(request.getClass());
        return creator.createFrom(request);
    }

    @SuppressWarnings("unchecked")
    <P extends Product, UR extends UpdateProductRequest, UC extends Product.UpdateCommand<?>>
    P updateProduct(P existingProduct, UR request) {
        ProductUpdater<P, UR, UC> updater = (ProductUpdater<P, UR, UC>) this.productUpdaters.get(request.getClass());
        return updater.update(existingProduct, request);
    }
}
