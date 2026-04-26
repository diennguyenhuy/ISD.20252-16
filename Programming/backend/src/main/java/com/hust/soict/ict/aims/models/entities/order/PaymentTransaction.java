package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_transaction")
@Getter
@NoArgsConstructor
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String transactionContent;
    private Instant transactionTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionMethod transactionMethod;

    @Column(nullable = false)
    private long amountPaid;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
