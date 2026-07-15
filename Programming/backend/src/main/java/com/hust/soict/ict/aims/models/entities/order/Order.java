package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.exceptions.OrderNotCompleteException;
import com.hust.soict.ict.aims.exceptions.OrderStateTransitionException;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.entities.VersionedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends VersionedEntity {
    public enum Status {
        DRAFT,
        PENDING,
        APPROVED,
        REJECTED,
        CANCELLED,
        REFUNDED;

        private static final Map<Status, EnumSet<Status>> transitions = Map.ofEntries(
                Map.entry(DRAFT, EnumSet.of(PENDING)),
                Map.entry(PENDING, EnumSet.of(APPROVED, REJECTED, CANCELLED)),
                Map.entry(APPROVED, EnumSet.noneOf(Status.class)),
                Map.entry(REJECTED, EnumSet.of(REFUNDED)),
                Map.entry(CANCELLED, EnumSet.of(REFUNDED)),
                Map.entry(REFUNDED, EnumSet.noneOf(Status.class))
        );

        boolean isValidTransitionTo(Status status) {
            return transitions.get(this).contains(status);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private BigDecimal totalWeight;

    @Column(nullable = false)
    private int totalItemCount;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private DeliveryInformation deliveryInformation;

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
            throw new OrderStateTransitionException("Cannot transition order status from " + status.name() + " to " + newStatus.name());
        }
        this.status = newStatus;
    }

    @Transient
    private long totalPriceWithoutVAT;
    @Transient
    private long totalPriceWithVAT;

    @Transient
    public Long getTotalAmount() {
        return invoice.getTotalAmount();
    }

    public boolean isCorrupted() {
        return items.size() != totalItemCount;
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

    private Order(Cart cart) {
        cart.getItems().forEach(item -> this.addItem(new OrderItem(item, this)));
        this.status = Status.DRAFT;
        this.totalItemCount = items.size();
        this.totalWeight = items.stream().map(OrderItem::getItemTotalWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalPriceWithoutVAT = items.stream().mapToLong(OrderItem::getItemTotalPrice).sum();
        this.totalPriceWithVAT = this.totalPriceWithoutVAT * 110 / 100;
    }

    public static class Draft {
        private final Order order;
        @Getter
        private final UUID checkoutId;
        @Getter
        private Long deliveryFee;

        public Draft(Cart cart) {
            this.order = new Order(cart);
            this.checkoutId = cart.getId();
        }

        public void provideDeliveryInformation(
                @NonNull String customerName,
                @NonNull String customerEmail,
                @NonNull String phoneNumber,
                @NonNull String province,
                @NonNull String commune,
                @NonNull String address,
                @NonNull String deliveryMethod
        ) {
            order.deliveryInformation = new DeliveryInformation(customerName, customerEmail, phoneNumber, province, commune, address, deliveryMethod, order);
        }

        public void provideDeliveryInformation(DeliveryInformation existingInfo, Long precalculatedDeliveryFee) {
            order.deliveryInformation = existingInfo != null ? new DeliveryInformation(
                    existingInfo.getCustomerName(),
                    existingInfo.getCustomerEmail(),
                    existingInfo.getPhoneNumber(),
                    existingInfo.getProvince(),
                    existingInfo.getCommune(),
                    existingInfo.getAddress(),
                    existingInfo.getDeliveryMethod(),
                    order
            ) : null;
            this.deliveryFee = precalculatedDeliveryFee;
            if (precalculatedDeliveryFee != null) {
                order.invoice = new Invoice(order, precalculatedDeliveryFee);
            }
        }

        public void updateDeliveryFee(long deliveryFee) {
            this.deliveryFee = deliveryFee;
            order.invoice = new Invoice(order, deliveryFee);
        }

        public void attachPaymentTransaction(PaymentTransaction paymentTransaction) {
            order.paymentTransaction = paymentTransaction;
            paymentTransaction.setOrder(order);
        }

        public Order complete() {
            if (order.deliveryInformation == null || order.invoice == null || order.paymentTransaction == null) {
                throw new OrderNotCompleteException("Order is not complete, cannot proceed to finish the order");
            }

            order.changeStatus(Status.PENDING);
            return order;
        }

        public List<OrderItem> getItems() {
            return order.getItems();
        }

        public DeliveryInformation getDeliveryInformation() {
            return order.deliveryInformation;
        }

        public Invoice getInvoice() {
            return order.invoice;
        }

        public Long getTotalPriceWithoutVAT() {
            return order.getTotalPriceWithoutVAT();
        }

        public Long getTotalPriceWithVAT() {
            return order.getTotalPriceWithVAT();
        }

        public BigDecimal getTotalWeight() {
            return order.getTotalWeight();
        }

        public Long getTotalAmount() {
            return deliveryFee + getTotalPriceWithVAT();
        }
    }
}
