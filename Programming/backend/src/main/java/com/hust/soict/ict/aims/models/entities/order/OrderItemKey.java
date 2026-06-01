package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Cohesion: Functional Cohesion
 * Reason:
 * Represents composite identity for OrderItem.
 * Coupling:
 * - Data coupling with OrderItem through identifier usage.
 */
@Deprecated
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemKey implements Serializable {
    private UUID orderId;
    private UUID productId;

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof OrderItemKey that) {
            return this.orderId.equals(that.orderId) && this.productId.equals(that.productId);
        }
        return false;
    }
}
