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

/**
 * Cohesion: Functional Cohesion
 * Reason:
 * Represents a purchased item within an Order and
 * related pricing/weight calculations.
 * Coupling:
 * - Stamp coupling with Order, Product, and CartItem
 *   through aggregate relationships and factory methods.
 */
@Entity
@Table(
        name = "order_item",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"order_id", "product_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Setter(AccessLevel.PACKAGE)
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

        this.productName = cartItem.getProduct().getTitle();
        this.unitPrice = cartItem.getProduct().getCurrentPrice();
        this.unitWeight = cartItem.getProduct().getWeight();
        this.quantity = cartItem.getQuantity();
    }
}
