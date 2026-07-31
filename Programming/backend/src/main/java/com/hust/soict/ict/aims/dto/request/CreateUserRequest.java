package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * Payload for the admin "create user" endpoint.
 * Password is NOT included — the system generates a temporary one
 * and sends it to the user via email.
 */
public record CreateUserRequest(
        @NotBlank
        String username,
        @NotBlank @Email
        String email,
        @NotEmpty
        Set<User.Role> roles
) {}
