package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.models.cart.Cart;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Order-aggregate",
                attributeNodes = {
                        @NamedAttributeNode(value = "items", subgraph = "OrderItem::product"),
                        @NamedAttributeNode("deliveryInformation"),
                        @NamedAttributeNode("invoice"),
                        @NamedAttributeNode("paymentTransaction")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "OrderItem::product",
                                attributeNodes = {
                                        @NamedAttributeNode("product")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "Order-summary",
                attributeNodes = {
                        @NamedAttributeNode("items")
                }
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PACKAGE)
    private DeliveryInformation deliveryInformation;

    @Setter
    private Long deliveryFee;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PACKAGE)
    private Invoice invoice;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PACKAGE)
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

    public void changeStatus(@NonNull OrderStatus newStatus) throws IllegalStateException {
        if (newStatus == status) return;

        if (!OrderStatus.transitions.get(status).contains(newStatus)) {
            throw new IllegalStateException("Cannot transition order status from " + status.name() + " to " + newStatus.name());
        }

        this.status = newStatus;
    }

    public static Order from(Cart cart) {
        Order order = new Order();
        cart.getItems().forEach(item -> order.addItem(OrderItem.from(item, order)));

        order.status = OrderStatus.DRAFT;

        return order;
    }
}
