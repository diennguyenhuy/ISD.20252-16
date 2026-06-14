package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.models.entities.product.Product;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    private final Map<Class<? extends Product>, ProductMapping<?, ?>> productMappings;

    public ProductMapper(List<ProductMapping<?, ?>> productMappings) {
        this.productMappings = productMappings.stream()
                .collect(Collectors.toMap(
                        ProductMapping::getProductClass,
                        Function.identity()
                ));
    }

    @SuppressWarnings("unchecked")
    public <P extends Product, D extends ProductDetail> D toProductDetail(P product) {
        ProductMapping<P, D> mapping = (ProductMapping<P, D>) this.productMappings.get(product.getClass());
        return mapping.map(product);
    }

    @SuppressWarnings("unchecked")
    public <P extends Product> ProductSummary toProductSummary(P product) {
        ProductMapping<P, ?> mapping = (ProductMapping<P, ?>) this.productMappings.get(product.getClass());
        return mapping.mapSummary(product);
    }
}
