package com.hust.soict.ict.aims.services.product;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.dto.response.CartResponse;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import com.hust.soict.ict.aims.dto.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Provide services for cart management
 */
@Service
@RequiredArgsConstructor
@Slf4j
class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final CartContext cartContext;
    private final CartMapper cartMapper;

    @Override
    public CartResponse getOrCreateCart() {
        return cartMapper.toCartResponse(cartContext.getOrCreateCart());
    }

    @Override
    public CartResponse addItem(UUID productId, int quantity) throws ProductNotFoundException, IllegalArgumentException {
        log.debug("Adding item to cart with quantity {} for product #{}...", quantity, productId);
        Product product = productRepository.findByIdAndStatus(productId, Product.Status.ACTIVE)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Cart cart = cartContext.getOrCreateCart();
        cart.addItem(product, quantity);

        log.debug("Item added successfully!");
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse updateItem(UUID productId, int quantity) throws NoSuchElementException, IllegalArgumentException {
        log.debug("Updating item with product id #{} to quantity {}...", productId, quantity);
        Cart cart = cartContext.getOrCreateCart();
        cart.updateItem(productId, quantity);
        log.debug("Item updated successfully!");
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse removeItem(UUID productId) {
        log.debug("Removing item from product with id #{}...", productId);
        Cart cart = cartContext.getOrCreateCart();
        cart.removeItem(productId);
        log.debug("Item removed successfully!");
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse clearCart() {
        log.debug("Clearing cart...");
        Cart cart = cartContext.getOrCreateCart();
        cart.clear();
        log.debug("Cart cleared successfully!");
        return cartMapper.toCartResponse(cart);
    }
}
