package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.cart.CartItem;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for checking product stock
 * Cohesion: Functional Cohesion
 * Reason:
 * All methods and logic contribute to validating
 * stock availability and cart consistency.
 * Coupling:
 * - Data coupling with ProductRepository through
 * repository method calls.
 * - Stamp coupling with CartContext, Cart, CartItem,
 * and Product because composite domain objects
 * are shared and traversed.
 */
@Component
@RequiredArgsConstructor
public class StockValidator {
    private final ProductRepository productRepository;

    /**
     * Check for stock availability.
     * This works by syncing products in cart with the repository.
     * @return the cart instance
     * @throws NotEnoughStockException if some products do not satisfy stock availability
     * @throws EmptyCartException if cart is empty
     * @throws ProductNotFoundException if a product "vanishes" (gets deactivated) during checkout
     */
    @Transactional(readOnly = true)
    public Cart checkStockAvailability(Cart cart) throws NotEnoughStockException, EmptyCartException, ProductNotFoundException {
        if (cart.isEmpty()) {
            throw new EmptyCartException();
        }

        List<UUID> productIds = cart.getItems().stream()
                .map(i -> i.getProduct().getId())
                .toList();

        List<Product> products = productRepository.findAllByIdInAndStatus(productIds, Product.Status.ACTIVE);

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        Map<UUID, Integer> insufficientQuantity = new HashMap<>();
        List<UUID> missingProducts = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            UUID productId = item.getProduct().getId();

            Product managedProduct = productMap.get(productId);

            if (managedProduct == null) {
                missingProducts.add(productId);
                continue;
            }

            cart.synchronizeProduct(managedProduct);

            if (!item.isStockAvailable()) {
                insufficientQuantity.put(productId, item.getProduct().getStockQuantity());
            }
        }

        for (UUID missing : missingProducts) {
            cart.removeItem(missing);
        }

        if (!insufficientQuantity.isEmpty() || !missingProducts.isEmpty()) {
            throw new NotEnoughStockException(insufficientQuantity, missingProducts);
        }

        return cart;
    }
}
