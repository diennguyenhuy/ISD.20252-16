package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdjustStockRequest(
        @NotNull(message = "Amount to adjust for must be provided")
        Integer delta,
        @NotBlank(message = "Reason for stock adjustment must be provided")
        String reason
) {}
