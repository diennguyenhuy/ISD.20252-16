package com.hust.soict.ict.aims.dto.response;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        Set<String> roles,
        boolean active,
        boolean blocked,
        boolean mustChangePassword,
        Instant createdAt,
        Instant updatedAt
) {}
