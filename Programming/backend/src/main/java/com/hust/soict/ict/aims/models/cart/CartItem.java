package com.hust.soict.ict.aims.models.cart;

import com.hust.soict.ict.aims.models.entities.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Cohesion: Functional Cohesion
 * Reason:
 * Represents a product entry within a shopping cart
 * and related quantity/price calculations.
 * Coupling:
 * - Stamp coupling with Product because full product
 *   objects are referenced and queried.
 * Note:
 * CartItem intentionally depends on live Product state,
 * unlike OrderItem which snapshots product data.
 */
@Getter
public class CartItem {
    private final Product product;
    @Setter
    private int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public long getItemTotalPrice() {
        return quantity * product.getCurrentPrice();
    }

    BigDecimal getItemTotalWeight() {
        return product.getWeight().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof CartItem that) {
            return this.product.getId().equals(that.product.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId());
    }
}
