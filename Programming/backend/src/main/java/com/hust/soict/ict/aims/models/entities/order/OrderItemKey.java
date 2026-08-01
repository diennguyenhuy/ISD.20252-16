package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(value = AccessLevel.PACKAGE)
public class OrderItemKey implements Serializable {
    @Column(updatable = false)
    private UUID orderId;
    @Column(updatable = false)
    private UUID productReferenceId;

    OrderItemKey(UUID productReferenceId) {
        this.productReferenceId = productReferenceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productReferenceId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof OrderItemKey that) {
            return Objects.equals(orderId, that.orderId) && Objects.equals(productReferenceId, that.productReferenceId);
        }
        return false;
    }
}
