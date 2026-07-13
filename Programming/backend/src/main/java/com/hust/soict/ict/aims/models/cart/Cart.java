package com.hust.soict.ict.aims.models.cart;

import com.hust.soict.ict.aims.models.entities.product.Product;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.*;

@NoArgsConstructor
public class Cart {
    private final Map<UUID, CartItem> items = new LinkedHashMap<>();

    /**
     * Add item to cart, if item already exists, increase the amount by the quantity
     * @param product The product entity to be added
     * @param quantity The quantity of the product to add to cart
     * @throws IllegalArgumentException If quantity <= 0
     */
    public void addItem(@NonNull Product product, int quantity) throws IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        items.merge(
                product.getId(),
                new CartItem(product, quantity),
                (existing, incoming) -> {
                    existing.setQuantity(existing.getQuantity() + incoming.getQuantity());
                    return existing;
                }
        );
    }

    /**
     * Update an item in cart with a new quantity. If the new quantity is 0, the item is removed from cart
     * @param productId The ID of the product to be updated
     * @param quantity The new quantity of the item
     * @throws NoSuchElementException If item does not exist in cart
     * @throws IllegalArgumentException If quantity < 0
     */
    public void updateItem(UUID productId, int quantity) throws NoSuchElementException, IllegalArgumentException {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }

        CartItem item = Optional.ofNullable(items.get(productId))
                .orElseThrow(() -> new NoSuchElementException("No product with id " + productId + " in cart"));

        if (quantity == 0) {
            items.remove(productId);
            return;
        }

        item.setQuantity(quantity);
    }

    /**
     * Remove the item from cart. Do nothing if it does not exist.
     * @param productId the product ID of the item
     */
    public void removeItem(UUID productId) {
        items.remove(productId);
    }

    public void synchronizeProduct(@NonNull Product product) {
        CartItem item = Optional.ofNullable(items.get(product.getId())).orElseThrow(() -> new NoSuchElementException("No product with id " + product.getId() + " in cart"));
        item.setProduct(product);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getQuantity(UUID productId) throws NoSuchElementException {
        return Optional.ofNullable(items.get(productId))
                .orElseThrow(() -> new NoSuchElementException("No product with id " + productId + " in cart"))
                .getQuantity();
    }

    public int getTotalQuantity() {
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public long getTotalPrice() {
        return items.values().stream()
                .mapToLong(CartItem::getItemTotalPrice)
                .sum();
    }

    public BigDecimal getTotalWeight() {
        return items.values().stream()
                .map(CartItem::getItemTotalWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Collection<CartItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }
}
