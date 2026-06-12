package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderApprovalException extends RuntimeException {
    @Getter
    private final Map<UUID, Integer> insufficientQuantity;
    @Getter
    private final List<String> missingProducts;

    public OrderApprovalException(Map<UUID, Integer> insufficientQuantity, List<String> missingProducts) {
        super("Some products in the order have quantities that exceed available stock, or have been deactivated. Cannot proceed to approve order.");
        this.insufficientQuantity = insufficientQuantity;
        this.missingProducts = missingProducts;
    }
}
