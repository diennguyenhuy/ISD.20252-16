package com.hust.soict.ict.aims.services.context;

import com.hust.soict.ict.aims.models.cart.Cart;

public interface CartContext {
    Cart getOrCreateCart();
    void setCart(Cart cart);
}
