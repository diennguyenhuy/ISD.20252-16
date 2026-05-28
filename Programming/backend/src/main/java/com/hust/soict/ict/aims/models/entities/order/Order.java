package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Cohesion: Communicational Cohesion
 * Reason:
 * Fields and methods operate on the same Order aggregate
 * and related business state.
 * Coupling:
 * - Stamp coupling with OrderItem, DeliveryInformation,
 *   Invoice, PaymentTransaction, and Cart through
 *   aggregate relationships.
 * - Data coupling with OrderStatus through enum-based
 *   state transition logic.
 */
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
public class Order extends AuditableEntity {
    public enum Status {
        DRAFT,
        PENDING,
        APPROVED,
        REJECTED,
        CANCELLED,
        REFUNDED;

        static final Map<Status, Set<Status>> transitions = Map.of(
                DRAFT, Set.of(PENDING),
                PENDING, Set.of(APPROVED, REJECTED, CANCELLED),
                APPROVED, Set.of(),
                REJECTED, Set.of(REFUNDED),
                CANCELLED, Set.of(REFUNDED),
                REFUNDED, Set.of()
        );
    }

    @Id
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PACKAGE)
    private DeliveryInformation deliveryInformation;

    @Setter
    @Transient
    private Long deliveryFee;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PACKAGE)
    private Invoice invoice;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PACKAGE)
    private PaymentTransaction paymentTransaction;

    @Transient
    public Long getTotalPriceWithoutVAT() {
        return items.stream().mapToLong(OrderItem::getItemTotalPrice).sum();
    }

    @Transient
    public Long getTotalPriceWithVAT() {
        return getTotalPriceWithoutVAT() * 110 / 100;
    }

    @Transient
    public BigDecimal getTotalWeight() {
        return items.stream().map(OrderItem::getItemTotalWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public Long getTotalAmount() {
        return deliveryFee + getTotalPriceWithVAT();
    }

    void addItem(OrderItem orderItem) {
        items.add(orderItem);
        orderItem.setOrder(this);
    }

    public void changeStatus(@NonNull Status newStatus) throws IllegalStateException {
        if (newStatus == status) return;

        if (!Status.transitions.get(status).contains(newStatus)) {
            throw new IllegalStateException("Cannot transition order status from " + status.name() + " to " + newStatus.name());
        }

        this.status = newStatus;
    }

    public static Order from(Cart cart) {
        Order order = new Order();
        cart.getItems().forEach(item -> order.addItem(OrderItem.from(item, order)));

        order.status = Status.DRAFT;

        return order;
    }
}
