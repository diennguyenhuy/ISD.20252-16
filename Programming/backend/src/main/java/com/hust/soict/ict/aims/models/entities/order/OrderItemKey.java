package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PROTECTED)
public class OrderItemKey implements Serializable {
    @Column(updatable = false)
    private UUID orderId;
    @Column(updatable = false)
    private UUID productId;

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof OrderItemKey that) {
            return Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId);
        }
        return false;
    }
}
