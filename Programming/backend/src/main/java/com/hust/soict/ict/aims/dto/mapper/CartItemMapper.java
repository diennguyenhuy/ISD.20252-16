package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.CartItemResponse;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.models.cart.CartItem;
import com.hust.soict.ict.aims.models.entities.product.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class CartItemMapper extends AbstractMapper<CartItem, CartItemResponse> {
    private final Map<Class<? extends Product>, ProductSummaryMapper<? extends Product>> productSummaryMapper;

    CartItemMapper(List<ProductSummaryMapper<? extends Product>> productSummaryMapper) {
        super(CartItemResponse::new);
        this.productSummaryMapper = productSummaryMapper.stream()
                .collect(
                        Collectors.toMap(
                                ProductSummaryMapper::getSourceClass,
                                Function.identity()
                        )
                );
    }

    @SuppressWarnings("unchecked")
    private <P extends Product> ProductSummary map(P product) {
        ProductSummaryMapper<P> productSummaryMapper = (ProductSummaryMapper<P>) this.productSummaryMapper.get(product.getClass());
        return productSummaryMapper.map(product);
    }

    @Override
    public void map(CartItem source, CartItemResponse target) {
        target.setProduct(map(source.getProduct()));
        target.setQuantity(source.getQuantity());
        target.setItemTotalPrice(source.getItemTotalPrice());
    }

    @Override
    public Class<CartItem> getSourceClass() {
        return CartItem.class;
    }

    @Override
    public Class<CartItemResponse> getTargetClass() {
        return CartItemResponse.class;
    }

}
