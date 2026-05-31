package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
/**
 * Entity representing a payment transaction
 *
 * Cohesion:
 * - Functional: stores transaction data
 *
 * Relationship:
 * - One-to-one with Order
 */
@Entity
@Table(name = "payment_transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentTransaction {
    public enum Method {
        VIETQR,
        PAYPAL
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String transactionContent;
    private Instant transactionTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Method transactionMethod;

    @Column(nullable = false)
    private Long amountPaid;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public static PaymentTransaction of(
            String transactionContent,
            Instant transactionTimestamp,
            Method transactionMethod,
            Long amountPaid,
            @NotNull Order order
    ) {
        PaymentTransaction pt = new PaymentTransaction();

        pt.transactionContent = transactionContent;
        pt.transactionTimestamp = transactionTimestamp;
        pt.transactionMethod = transactionMethod;
        pt.amountPaid = amountPaid;
        pt.order = order;
        order.setPaymentTransaction(pt);

        return pt;
    }
}

