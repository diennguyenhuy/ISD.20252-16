package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.models.entities.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItem {
    @EmbeddedId
    private OrderItemKey id;

    @ManyToOne(optional = false)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(optional = false)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String productName;

    private int quantity;
    private long unitPrice;

    @Column(precision = 10, scale = 3)
    private BigDecimal unitWeight;

    public OrderItem(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;

        this.id = new OrderItemKey(order.getId(), product.getId());
        this.productName = product.getTitle();
        this.unitPrice = product.getCurrentPrice();
        this.unitWeight = product.getWeight();
    }

    @Transient
    long getItemTotalPrice() {
        return unitPrice * quantity;
    }

    @Transient
    BigDecimal getItemTotalWeight() {
        return unitWeight.multiply(BigDecimal.valueOf(quantity));
    }
}
