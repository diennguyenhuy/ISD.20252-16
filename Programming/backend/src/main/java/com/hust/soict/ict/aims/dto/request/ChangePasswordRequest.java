package com.hust.soict.ict.aims.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for the self-service "change my password" endpoint.
 */
public record ChangePasswordRequest(
        @NotBlank
        String currentPassword,
        @NotBlank
        @Size(min = 8, message = "New password must be at least 8 characters")
        String newPassword,
        @NotBlank
        String confirmPassword
) {}
