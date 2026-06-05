package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductCreatorFactory {
    private final Map<Class<? extends CreateProductRequest>, ProductCreator<?, ?>> productCreators;

    public ProductCreatorFactory(List<ProductCreator<?, ?>> productCreators) {
        this.productCreators = productCreators.stream().collect(Collectors.toMap(
                ProductCreator::createRequestType,
                Function.identity()
        ));
    }

    @SuppressWarnings("unchecked")
    public <P extends Product, C extends CreateProductRequest>
    ProductCreator<P, C> getCreator(C request) {
        return (ProductCreator<P, C>) productCreators.get(request.getClass());
    }
}
