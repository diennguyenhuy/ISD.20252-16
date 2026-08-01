package com.hust.soict.ict.aims.dto.response.cart;

import com.hust.soict.ict.aims.dto.response.product.ProductMappers;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.cart.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    private final ProductMappers productMappers;

    private CartMapper(ProductMappers productMappers) {
        this.productMappers = productMappers;
    }

    public CartResponse map(Cart cart) {
        if (cart == null) {
            return null;
        }

        return new CartResponse(
                cart.getItems().stream().map(this::map).toList(),
                cart.getTotalQuantity(),
                cart.getTotalPrice()
        );
    }

    public CartItemResponse map(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        return new CartItemResponse(
                productMappers.mapSummary(cartItem.getProduct()),
                cartItem.getQuantity(),
                cartItem.getItemTotalPrice()
        );
    }
}
