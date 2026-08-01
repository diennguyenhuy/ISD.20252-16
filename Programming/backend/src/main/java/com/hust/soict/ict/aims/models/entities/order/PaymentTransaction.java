package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String transactionContent;
    private Instant transactionTimestamp;

    @Column(nullable = false)
    private String transactionMethod;

    @Column(nullable = false)
    private Long amountPaid;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Setter(AccessLevel.PACKAGE)
    private Order order;

    public static PaymentTransaction of(
            String transactionContent,
            Instant transactionTimestamp,
            String transactionMethod,
            Long amountPaid
    ) {
        PaymentTransaction pt = new PaymentTransaction();

        pt.transactionContent = transactionContent;
        pt.transactionTimestamp = transactionTimestamp;
        pt.transactionMethod = transactionMethod;
        pt.amountPaid = amountPaid;

        return pt;
    }
}
