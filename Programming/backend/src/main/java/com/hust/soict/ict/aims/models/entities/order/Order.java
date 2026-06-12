package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.exceptions.OrderNotCompleteException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.entities.VersionedEntity;
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
@Table(name = "\"order\"")
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
                        @NamedAttributeNode("deliveryInformation"),
                        @NamedAttributeNode("invoice")
                }
        )
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends VersionedEntity {
    public enum Status {
        DRAFT,
        PENDING,
        APPROVED,
        REJECTED,
        CANCELLED,
        REFUNDED;

        private static final Map<Status, Set<Status>> transitions = Map.ofEntries(
                Map.entry(DRAFT, Set.of(PENDING)),
                Map.entry(PENDING, Set.of(APPROVED, REJECTED, CANCELLED)),
                Map.entry(APPROVED, Set.of()),
                Map.entry(REJECTED, Set.of(REFUNDED)),
                Map.entry(CANCELLED, Set.of(REFUNDED)),
                Map.entry(REFUNDED, Set.of())
        );

        boolean isValidTransitionTo(Status status) {
            return transitions.get(this).contains(status);
        }
    }

    @Id
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private DeliveryInformation deliveryInformation;

    @Transient
    private Long deliveryFee;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Invoice invoice;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PACKAGE)
    private PaymentTransaction paymentTransaction;

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void changeStatus(Status newStatus) throws OrderStateTransitionException {
        if (!this.status.isValidTransitionTo(newStatus)) {
            throw new OrderStateTransitionException("Cannot transition order status map " + status.name() + " to " + newStatus.name());
        }
        this.status = newStatus;
    }

    public DeliveryInformation provideDeliveryInformation(
            @NonNull String customerName,
            @NonNull String customerEmail,
            @NonNull String phoneNumber,
            @NonNull String province,
            @NonNull String commune,
            @NonNull String address,
            @NonNull String deliveryMethod
    ) {
        this.deliveryInformation = new DeliveryInformation(customerName, customerEmail, phoneNumber, province, commune, address, deliveryMethod, this);
        return this.deliveryInformation;
    }

    public void provideDeliveryInformation(@NonNull DeliveryInformation existingInfo, long precalculatedDeliveryFee) {
        this.deliveryInformation = new DeliveryInformation(
                existingInfo.getCustomerName(),
                existingInfo.getCustomerEmail(),
                existingInfo.getPhoneNumber(),
                existingInfo.getProvince(),
                existingInfo.getCommune(),
                existingInfo.getAddress(),
                existingInfo.getDeliveryMethod(),
                this
        );
        this.deliveryFee = precalculatedDeliveryFee;
    }

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
        if (status != Status.DRAFT) {
            return invoice.getTotalAmount();
        }
        return deliveryFee + getTotalPriceWithVAT();
    }

    public void updateDeliveryFee(long deliveryFee) {
        if (status != Status.DRAFT) {
            throw new UnsupportedOperationException("Operation Update Delivery Fee is unavailable for placed non-draft order");
        }
        this.deliveryFee = deliveryFee;
        this.invoice = new Invoice(this);
    }

    private boolean isComplete() {
        return (deliveryInformation != null && deliveryFee != null && invoice != null && paymentTransaction != null)
                || status != Status.DRAFT;
    }

    public void complete() {
        if (!isComplete()) {
            throw new OrderNotCompleteException("Cannot check out order when order is not completed!");
        }
        changeStatus(Status.PENDING);
    }
    public void approve() {
        changeStatus(Status.APPROVED);
    }
    public void reject() {
        changeStatus(Status.REJECTED);
    }
    public void cancel() {
        changeStatus(Status.CANCELLED);
    }
    public void refund() {
        changeStatus(Status.REFUNDED);
    }

    void addItem(OrderItem orderItem) {
        items.add(orderItem);
        orderItem.setOrder(this);
    }
    public static Order from(Cart cart) {
        Order order = new Order();
        cart.getItems().forEach(item -> order.addItem(OrderItem.from(item, order)));

        order.status = Status.DRAFT;

        return order;
    }
}
