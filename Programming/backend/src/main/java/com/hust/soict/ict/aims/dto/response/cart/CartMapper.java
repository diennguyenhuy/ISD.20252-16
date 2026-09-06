package com.hust.soict.ict.aims.dto.response.cart;

import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.cart.CartItem;

public final class CartMapper {
    private CartMapper() {}

    public static CartResponse map(Cart cart) {
        if (cart == null) {
            return null;
        }

        return new CartResponse(
                cart.getItems().stream().map(CartMapper::map).toList(),
                cart.getTotalQuantity(),
                cart.getTotalPrice()
        );
    }

    public static CartItemResponse map(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        return new CartItemResponse(
                cartItem.getProduct().getId(),
                cartItem.getProduct().getTitle(),
                cartItem.getProduct().getStockQuantity(),
                cartItem.getProduct().getImageURL().orElse(null),
                cartItem.getProduct().getClass().getSimpleName(),
                cartItem.getQuantity(),
                cartItem.getProduct().getCurrentPrice(),
                cartItem.getItemTotalPrice()
        );
    }
}
