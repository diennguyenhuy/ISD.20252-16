package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.models.cart.CartItem;
import com.hust.soict.ict.aims.models.entities.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @EmbeddedId
    private OrderItemKey id = new OrderItemKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    @Setter(AccessLevel.PACKAGE)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Long unitPrice;

    @Column(precision = 10, scale = 3, nullable = false)
    private BigDecimal unitWeight;

    @Transient
    public Long getItemTotalPrice() {
        return unitPrice * quantity;
    }

    @Transient
    BigDecimal getItemTotalWeight() {
        return unitWeight.multiply(BigDecimal.valueOf(quantity));
    }

    OrderItem(CartItem cartItem, Order order) {
        this.order = order;
        this.product = cartItem.getProduct();

        this.productName = cartItem.getProduct().getTitle();
        this.unitPrice = cartItem.getProduct().getCurrentPrice();
        this.unitWeight = cartItem.getProduct().getWeight();
        this.quantity = cartItem.getQuantity();
    }
}
