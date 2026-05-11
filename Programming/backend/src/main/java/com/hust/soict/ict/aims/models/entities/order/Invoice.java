package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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

    public static Invoice from(@NonNull Order order) {
        Invoice invoice = new Invoice();

        order.setInvoice(invoice);
        invoice.order = order;
        invoice.totalPriceWithoutVAT = order.getTotalPriceWithoutVAT();
        invoice.totalPriceWithVAT = order.getTotalPriceWithVAT();
        invoice.deliveryFee = order.getDeliveryFee();
        invoice.totalAmount = order.getTotalAmount();
        invoice.issuedAt = Instant.now();

        return invoice;
    }
}
