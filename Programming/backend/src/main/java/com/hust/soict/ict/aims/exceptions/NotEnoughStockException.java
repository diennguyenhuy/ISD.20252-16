package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Exception thrown when cart demands exceed stock quantity.
 * Contains a map of product IDs to its actual current stock,
 * and a list of product that have vanished from the repository.
 */
public class NotEnoughStockException extends RuntimeException {
    @Getter
    private final Map<UUID, Integer> insufficientQuantity;
    @Getter
    private final List<UUID> missingProducts;

    public NotEnoughStockException(Map<UUID, Integer> insufficientQuantity, List<UUID> missingProducts) {
        super("Some products in your cart have quantities that exceed available stock, or have been deactivated. Please adjust before proceeding with checkout.");
        this.insufficientQuantity = insufficientQuantity;
        this.missingProducts = missingProducts;
    }

}
