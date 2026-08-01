package com.hust.soict.ict.aims.models.cart;

import com.hust.soict.ict.aims.models.entities.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class CartItem {
    @Setter(AccessLevel.PACKAGE)
    private Product product;
    @Setter(AccessLevel.PACKAGE)
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

    public boolean isStockAvailable() {
        return product.getStockQuantity() >= quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId());
    }
}
