package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.models.cart.CartItem;
import com.hust.soict.ict.aims.models.entities.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(
        name = "order_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "product_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @EmbeddedId
    private OrderItemKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
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
        this.id = new OrderItemKey(cartItem.getProduct().getId());

        this.productName = cartItem.getProduct().getTitle();
        this.unitPrice = cartItem.getProduct().getCurrentPrice();
        this.unitWeight = cartItem.getProduct().getWeight();
        this.quantity = cartItem.getQuantity();
    }
}
