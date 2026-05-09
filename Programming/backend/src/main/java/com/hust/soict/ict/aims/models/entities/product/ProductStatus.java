package com.hust.soict.ict.aims.models.entities.product;

import java.util.Map;
import java.util.Set;

public enum ProductStatus {
    ACTIVE,
    DEACTIVATED,
    DELETED;

    static final Map<ProductStatus, Set<ProductStatus>> transitions = Map.of(
            ACTIVE, Set.of(DEACTIVATED, DELETED),
            DEACTIVATED, Set.of(ACTIVE, DELETED),
            DELETED, Set.of(ACTIVE)
    );
}
