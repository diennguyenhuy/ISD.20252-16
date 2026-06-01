package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
/*
 * [SOLID OCP: minor, localized note][cite: 1]
 * Principle: Open/Closed (O)[cite: 1]
 * Why: Method is a fixed enum {VIETQR, PAYPAL}. Adding a provider (Stripe/Momo)[cite: 1]
 *      means editing this enum and any switch reading it. The provider call path[cite: 1]
 *      itself is OCP-clean (IPaymentProvider), so this is a small touch point, not[cite: 1]
 *      a structural violation. For a known, small, closed set an enum is acceptab][cite: 1]
 * Proposed Solution: If methods become open-ended, store a provider code resolved[cite: 1]
 *      from the IPaymentProvider implementation rather than a hardcoded enum.[cite: 1]
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

