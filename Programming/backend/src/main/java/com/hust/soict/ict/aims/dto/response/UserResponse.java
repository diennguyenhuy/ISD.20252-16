package com.hust.soict.ict.aims.dto.response;

import com.hust.soict.ict.aims.models.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Read-only projection of a {@link User} for admin endpoints.
 * Deliberately excludes the hashed password.
 */
@Data
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private Set<User.Role> roles;
    private boolean active;
    private boolean blocked;
    private boolean mustChangePassword;
    private Instant createdAt;
    private Instant updatedAt;

    /** Convenience factory method — keeps mapping logic in one place. */
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles(),
                user.isActive(),
                user.isBlocked(),
                user.isMustChangePassword(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
