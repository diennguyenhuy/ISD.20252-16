package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

/**
 * Payload for the admin "create user" endpoint.
 * Password is NOT included — the system generates a temporary one
 * and sends it to the user via email.
 */
@Data
public class CreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank @Email
    private String email;

    @NotEmpty
    private Set<User.Role> roles;
}
