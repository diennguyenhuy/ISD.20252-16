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
public class ProductFactory {
    private final Map<Class<? extends CreateProductRequest>, ProductCreator<?, ?>> productCreators;
    private final Map<Class<? extends UpdateProductRequest>, ProductUpdater<?, ?>> productUpdaters;

    public ProductFactory(
            List<ProductCreator<?, ?>> productCreators,
            List<ProductUpdater<?, ?>> productUpdaters
    ) {
        this.productCreators = productCreators.stream()
                .collect(Collectors.toMap(
                        ProductCreator::createRequestType,
                        Function.identity()
                ));
        this.productUpdaters = productUpdaters.stream()
                .collect(Collectors.toMap(
                        ProductUpdater::updateRequestType,
                        Function.identity()
                ));
    }

    @SuppressWarnings("unchecked")
    public <P extends Product, C extends CreateProductRequest>
    P createProduct(C request) {
        ProductCreator<P, C> creator = (ProductCreator<P, C>) productCreators.get(request.getClass());
        return creator.createFrom(request);
    }

    @SuppressWarnings("unchecked")
    public <P extends Product, U extends UpdateProductRequest>
    P updateProduct(P existingProduct, U request) {
        ProductUpdater<P, U> updater = (ProductUpdater<P, U>) this.productUpdaters.get(request.getClass());
        return updater.updateFrom(existingProduct, request);
    }
}
