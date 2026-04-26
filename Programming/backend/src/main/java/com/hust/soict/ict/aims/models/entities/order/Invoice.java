package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoice")
@Getter
@NoArgsConstructor
public class Invoice {
    @Id
    @Column(updatable = false, name = "order_id")
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    @CreationTimestamp
    @UpdateTimestamp
    private Instant issuedAt;

    @Column(name = "total_price_without_vat", nullable = false)
    private long totalPriceWithoutVAT;
    @Column(name = "total_price_with_vat", nullable = false)
    private long totalPriceWithVAT;
    @Column(nullable = false)
    private long deliveryFee;
    @Column(nullable = false)
    private long totalAmount;

    public Invoice(Order order) {
        this.id = order.getId();
        this.order = order;
        this.totalPriceWithoutVAT = order.getTotalPriceWithoutVAT();
        this.totalPriceWithVAT = order.getTotalPriceWithVAT();
        this.deliveryFee = order.getDeliveryFee();
        this.totalAmount = order.getTotalAmount();
    }
}
