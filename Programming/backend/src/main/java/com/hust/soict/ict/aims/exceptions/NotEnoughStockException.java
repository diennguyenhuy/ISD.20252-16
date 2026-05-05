package com.hust.soict.ict.aims.exceptions;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;

public class NotEnoughStockException extends RuntimeException {
    @Getter
    private final Map<UUID, Integer> insufficientQuantity;

    public NotEnoughStockException(Map<UUID, Integer> insufficientQuantity) {
        super("Some products in your cart have quantities that exceed available stock. Please adjust before proceeding with checkout.");
        this.insufficientQuantity = insufficientQuantity;
    }

}
