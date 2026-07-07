package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Cohesion: Functional Cohesion
 * Reason:
 * Represents invoice data and invoice creation logic
 * for an Order.
 * Coupling:
 * - Stamp coupling with Order because invoice data
 *   is derived from the Order aggregate.
 * Design Strength:
 * Captures financial values as snapshots, reducing
 * dependency on future Order state changes.
 */
@Entity
@Table(name = "invoice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invoice {
    @Id
    @Column(updatable = false, name = "order_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    private Instant issuedAt;

    @Column(name = "total_price_without_vat", nullable = false)
    private Long totalPriceWithoutVAT;
    @Column(name = "total_price_with_vat", nullable = false)
    private Long totalPriceWithVAT;
    @Column(nullable = false)
    private Long deliveryFee;
    @Column(nullable = false)
    private Long totalAmount;

    Invoice(Order order, long deliveryFee) {
        this.order = order;
        this.totalPriceWithoutVAT = order.getTotalPriceWithoutVAT();
        this.totalPriceWithVAT = order.getTotalPriceWithVAT();
        this.deliveryFee = deliveryFee;
        this.totalAmount = order.getTotalPriceWithVAT() + deliveryFee;
        this.issuedAt = Instant.now();
    }
}
