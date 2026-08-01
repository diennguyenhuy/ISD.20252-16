package com.hust.soict.ict.aims.dto.response.product;

import com.hust.soict.ict.aims.models.entities.product.Product;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductMappers {
    private final Map<Class<? extends Product>, ProductMapper<?, ?>> productMappers = new HashMap<>();

    public ProductMappers(List<ProductMapper<?, ?>> productMappers) {
        productMappers.forEach(mapper -> this.productMappers.put(mapper.productClass, mapper));
    }

    @SuppressWarnings("unchecked")
    public <P extends Product, D extends ProductDetail> D map(P product) {
        ProductMapper<P, D> productMapper = (ProductMapper<P, D>) productMappers.get(product.getClass());
        return productMapper.map(product);
    }

    @SuppressWarnings("unchecked")
    public <P extends Product> ProductSummary mapSummary(P product) {
        ProductMapper<P, ?> productMapper = (ProductMapper<P, ?>) productMappers.get(product.getClass());
        return productMapper.mapSummary(product);
    }
}
