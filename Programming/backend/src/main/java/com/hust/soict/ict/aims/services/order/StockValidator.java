package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.cart.CartItem;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.product.ProductStatus;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for checking product stock
 */
@Component
@RequiredArgsConstructor
public class StockValidator {
    private final ProductRepository productRepository;
    private final CartContext cartContext;

    /**
     * Check for stock availability
     * @return the cart instance
     * @throws NotEnoughStockException if some products do not satisfy stock availability
     * @throws EmptyCartException if cart is empty
     * @throws ProductNotFoundException if product "vanishes" (gets deactivated) during checkout
     */
    @Transactional
    public Cart checkStockAvailability() throws NotEnoughStockException, EmptyCartException, ProductNotFoundException {
        Cart cart = cartContext.getOrCreateCart();

        if (cart.isEmpty()) {
            throw new EmptyCartException();
        }

        List<UUID> productIds = cart.getItems().stream()
                .map(i -> i.getProduct().getId())
                .toList();

        List<Product> products = productRepository.findAllByIdInAndStatus(productIds, ProductStatus.ACTIVE);

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        Map<UUID, Integer> insufficientQuantity = new HashMap<>();

        for (CartItem item : cart.getItems()) {
            Product managedProduct = productMap.get(item.getProduct().getId());

            if (managedProduct != null) {
                item = new CartItem(managedProduct, item.getQuantity());
                if (item.getProduct().getStockQuantity() < item.getQuantity()) {
                    insufficientQuantity.put(item.getProduct().getId(), item.getProduct().getStockQuantity());
                }
            } else {
                cart.removeItem(item.getProduct().getId());
                throw new ProductNotFoundException(item.getProduct().getId());
            }

        }

        if (!insufficientQuantity.isEmpty()) {
            throw new NotEnoughStockException(insufficientQuantity);
        }

        return cart;
    }
}
