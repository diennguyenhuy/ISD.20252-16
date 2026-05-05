package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.cart.CartItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter
    private DeliveryInformation deliveryInformation;

    @Setter
    private Long deliveryFee;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter
    private Invoice invoice;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private PaymentTransaction paymentTransaction;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Transient
    public Long getTotalPriceWithoutVAT() {
        return items.stream().mapToLong(OrderItem::getItemTotalPrice).sum();
    }

    @Transient
    public Long getTotalPriceWithVAT() {
        return Math.round(getTotalPriceWithoutVAT() * 1.1);
    }

    @Transient
    public BigDecimal getTotalWeight() {
        return items.stream().map(OrderItem::getItemTotalWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public Long getTotalAmount() {
        return deliveryFee + getTotalPriceWithVAT();
    }

    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
    }

    public static Order from(Cart cart) {
        Order order = new Order();
        cart.getItems().forEach(item -> order.addItem(OrderItem.from(item, order)));

        order.status = OrderStatus.DRAFT;

        return order;
    }
}
