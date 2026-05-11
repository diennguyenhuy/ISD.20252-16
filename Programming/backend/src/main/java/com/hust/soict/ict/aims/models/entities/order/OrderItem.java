package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.models.cart.CartItem;
import com.hust.soict.ict.aims.models.entities.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @EmbeddedId
    private OrderItemKey id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String productName;

    private Integer quantity;
    private Long unitPrice;

    @Column(precision = 10, scale = 3)
    private BigDecimal unitWeight;

    @Transient
    public Long getItemTotalPrice() {
        return unitPrice * quantity;
    }

    @Transient
    BigDecimal getItemTotalWeight() {
        return unitWeight.multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItem from(CartItem cartItem, Order order) {
        OrderItem item = new OrderItem();

        item.order = order;
        item.product = cartItem.getProduct();

        item.productName = cartItem.getProduct().getTitle();
        item.unitPrice = cartItem.getProduct().getCurrentPrice();
        item.unitWeight = cartItem.getProduct().getWeight();
        item.quantity = cartItem.getQuantity();

        return item;
    }
}
