package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.CartResponse;
import com.hust.soict.ict.aims.models.cart.Cart;
import org.springframework.stereotype.Component;

@Component
class CartMapper extends AbstractMapper<Cart, CartResponse> {
    private final CartItemMapper cartItemMapper;

    CartMapper(CartItemMapper cartItemMapper) {
        super(Cart.class, CartResponse.class, CartResponse::new);
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    protected void map(Cart source, CartResponse target) {
        target.setItems(source.getItems().stream().map(cartItemMapper::map).toList());
        target.setTotalPrice(source.getTotalPrice());
        target.setTotalQuantity(source.getTotalQuantity());
    }
}
