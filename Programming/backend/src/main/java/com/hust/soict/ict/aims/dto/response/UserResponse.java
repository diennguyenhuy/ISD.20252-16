package com.hust.soict.ict.aims.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private Set<String> roles;
    private boolean active;
    private boolean blocked;
    private boolean mustChangePassword;
    private Instant createdAt;
    private Instant updatedAt;
}
