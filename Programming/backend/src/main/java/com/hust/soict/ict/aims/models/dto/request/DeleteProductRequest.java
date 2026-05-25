package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class DeleteProductRequest {
    @NotEmpty(message = "Product ID list cannot be empty")
    private List<UUID> productIds;
}