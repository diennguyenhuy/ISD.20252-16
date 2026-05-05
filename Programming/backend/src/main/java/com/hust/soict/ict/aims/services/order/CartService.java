package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.services.context.CartContext;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.dto.response.CartResponse;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import com.hust.soict.ict.aims.models.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Provide services for cart management
 */
@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private final CartContext cartContext;
    private final CartMapper cartMapper;

    public CartResponse getOrCreateCart() {
        return cartMapper.toCartResponse(cartContext.getOrCreateCart());
    }

    public CartResponse addItem(UUID productId, int quantity) throws ProductNotFoundException, IllegalArgumentException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Cart cart = cartContext.getOrCreateCart();

        cart.addItem(product, quantity);

        return cartMapper.toCartResponse(cart);
    }

    public CartResponse updateItem(UUID productId, int quantity) throws NoSuchElementException, IllegalArgumentException {
        Cart cart = cartContext.getOrCreateCart();
        cart.updateItem(productId, quantity);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse removeItem(UUID productId) {
        Cart cart = cartContext.getOrCreateCart();
        cart.removeItem(productId);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse clearCart() {
        Cart cart = cartContext.getOrCreateCart();
        cart.clear();
        return cartMapper.toCartResponse(cart);
    }
}
