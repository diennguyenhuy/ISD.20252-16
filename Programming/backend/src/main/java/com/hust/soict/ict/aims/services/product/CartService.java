package com.hust.soict.ict.aims.services.product;

import com.hust.soict.ict.aims.dto.response.CartResponse;

import java.util.NoSuchElementException;
import java.util.UUID;

public interface CartService {
    CartResponse getOrCreateCart();
    CartResponse addItem(UUID productId, int quantity);
    CartResponse updateItem(UUID productId, int quantity) throws NoSuchElementException, IllegalArgumentException;
    CartResponse removeItem(UUID productId);
    CartResponse clearCart();
}
