package com.hust.soict.ict.aims.services.product;

import com.hust.soict.ict.aims.models.cart.Cart;

import java.util.NoSuchElementException;
import java.util.UUID;

public interface CartService {
    Cart getOrCreateCart();
    Cart addItem(UUID productId, int quantity);
    Cart updateItem(UUID productId, int quantity) throws NoSuchElementException, IllegalArgumentException;
    Cart removeItem(UUID productId);
    Cart clearCart();
}
