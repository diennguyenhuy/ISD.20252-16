package com.hust.soict.ict.aims.dto.response;

import java.util.List;
import java.util.UUID;

/**
 * Returned by the /api/auth/login endpoint.
 *
 * {@code mustChangePassword} is true when an admin has reset the user's password.
 * The frontend should redirect the user to the "change password" screen before
 * allowing further navigation.
 */
public record JwtResponse(
        String token,
        String type,
        UUID id,
        String username,
        String email,
        List<String> roles,
        boolean mustChangePassword
) {
    public JwtResponse(String token, UUID id, String username, String email,
                       List<String> roles, boolean mustChangePassword) {
        this(token, "Bearer", id, username, email, roles, mustChangePassword);
    }
}