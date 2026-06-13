package com.hust.soict.ict.aims.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Returned by the /api/auth/login endpoint.
 *
 * {@code mustChangePassword} is true when an admin has reset the user's password.
 * The frontend should redirect the user to the "change password" screen before
 * allowing further navigation.
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;
    /** Signals that the user must change their password before performing any actions. */
    private boolean mustChangePassword;

    public JwtResponse(String token, UUID id, String username, String email,
                       List<String> roles, boolean mustChangePassword) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.mustChangePassword = mustChangePassword;
    }
}