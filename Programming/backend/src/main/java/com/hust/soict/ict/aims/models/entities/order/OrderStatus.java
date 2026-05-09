package com.hust.soict.ict.aims.models.entities.order;

import java.util.Map;
import java.util.Set;

public enum OrderStatus {
    DRAFT,
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED,
    REFUNDED;

    static final Map<OrderStatus, Set<OrderStatus>> transitions = Map.of(
            DRAFT, Set.of(PENDING),
            PENDING, Set.of(APPROVED, REJECTED, CANCELLED),
            APPROVED, Set.of(),
            REJECTED, Set.of(REFUNDED),
            CANCELLED, Set.of(REFUNDED),
            REFUNDED, Set.of()
    );
}
