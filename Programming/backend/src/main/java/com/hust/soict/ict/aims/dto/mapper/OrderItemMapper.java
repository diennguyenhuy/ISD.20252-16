package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.order.OrderItemResponse;
import com.hust.soict.ict.aims.models.entities.order.OrderItem;
import org.springframework.stereotype.Component;

@Component
class OrderItemMapper extends AbstractMapper<OrderItem, OrderItemResponse> {

    OrderItemMapper() {
        super(OrderItem.class, OrderItemResponse.class, OrderItemResponse::new);
    }

    @Override
    protected void map(OrderItem source, OrderItemResponse target) {
        if (source.getProduct() != null) {
            target.setProductId(source.getProduct().getId());
            target.setProductImage(source.getProduct().getImageURL());
        }
        target.setProductReferenceId(source.getId().getProductReferenceId());
        target.setProductName(source.getProductName());
        target.setQuantity(source.getQuantity());
        target.setUnitPrice(source.getUnitPrice());
        target.setItemTotalPrice(source.getItemTotalPrice());
    }
}
