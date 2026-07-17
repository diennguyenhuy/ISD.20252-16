package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.CartResponse;
import com.hust.soict.ict.aims.models.cart.Cart;
import org.springframework.stereotype.Component;

@Component
class CartMapper extends AbstractMapper<Cart, CartResponse> {
    private final CartItemMapper cartItemMapper;

    CartMapper(CartItemMapper cartItemMapper) {
        super(CartResponse::new);
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public void map(Cart source, CartResponse target) {
        target.setItems(source.getItems().stream().map(cartItemMapper::map).toList());
        target.setTotalPrice(source.getTotalPrice());
        target.setTotalQuantity(source.getTotalQuantity());
    }

    @Override
    public Class<Cart> getSourceClass() {
        return Cart.class;
    }

    @Override
    public Class<CartResponse> getTargetClass() {
        return CartResponse.class;
    }

}
