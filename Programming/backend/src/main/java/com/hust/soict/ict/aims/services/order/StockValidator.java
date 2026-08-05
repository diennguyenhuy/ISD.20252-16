package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for checking product stock
 */
@Component
@RequiredArgsConstructor
class StockValidator {
    private final ProductRepository productRepository;

    /**
     * Check for stock availability.
     * This works by syncing products in cart with the repository.
     * @throws NotEnoughStockException if some products do not satisfy stock availability
     * @throws EmptyCartException if cart is empty
     * @throws ProductNotFoundException if a product "vanishes" (gets deactivated) during checkout
     */
    @Transactional(readOnly = true)
    void checkStockAvailability(Cart cart) throws NotEnoughStockException, EmptyCartException {
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

        cart.getItems().forEach(i -> {
            UUID productId = i.getProduct().getId();

            Product managedProduct = productMap.get(productId);

            if (managedProduct == null) {
                missingProducts.add(productId);
                return;
            }

            cart.synchronizeProduct(managedProduct);

            if (!i.isStockAvailable()) {
                insufficientQuantity.put(productId, i.getProduct().getStockQuantity());
            }
        });

        if (!insufficientQuantity.isEmpty() || !missingProducts.isEmpty()) {
            missingProducts.forEach(cart::removeItem);
            throw new NotEnoughStockException(insufficientQuantity, missingProducts);
        }
    }
}
