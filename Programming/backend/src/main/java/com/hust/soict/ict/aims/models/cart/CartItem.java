package com.hust.soict.ict.aims.models.cart;

import com.hust.soict.ict.aims.models.entities.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class CartItem {
    private final Product product;
    @Setter
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    long getItemTotalPrice() {
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
