package com.hust.soict.ict.aims.dto.response;

import com.hust.soict.ict.aims.models.entities.user.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(User.Role::name).collect(Collectors.toSet()),
                user.isActive(),
                user.isBlocked(),
                user.isMustChangePassword(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
