package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdjustStockRequest {
    @NotNull(message = "Amount to adjust for must be provided")
    private Integer delta;
    @NotBlank(message = "Reason for stock adjustment must be provided")
    private String reason;
}
