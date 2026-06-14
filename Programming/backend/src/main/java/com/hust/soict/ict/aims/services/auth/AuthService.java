package com.hust.soict.ict.aims.services.auth;

import com.hust.soict.ict.aims.dto.request.LoginRequest;
import com.hust.soict.ict.aims.dto.response.JwtResponse;
import com.hust.soict.ict.aims.exceptions.AccountDeactivatedException;
import com.hust.soict.ict.aims.security.jwt.JwtUtils;
import com.hust.soict.ict.aims.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles authentication business logic.
 *
 * Responsibilities:
 *  - Delegate credential verification to Spring Security's AuthenticationManager.
 *  - Enforce account status rules (deactivated accounts are rejected here with a
 *    clear error message; blocked accounts are allowed to log in but restricted
 *    at the service layer).
 *  - Build and return the JwtResponse.
 *
 * The controller layer has no knowledge of JWT internals or user details.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Authenticates a user and returns a signed JWT along with the user's profile.
     *
     * @param request login credentials
     * @return a {@link JwtResponse} containing the JWT and basic user info
     * @throws AccountDeactivatedException if the account has been deactivated by an admin
     */
    public JwtResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (DisabledException ex) {
            // Spring Security throws DisabledException when isEnabled() returns false (deactivated account).
            // We re-wrap it into our own typed exception so the GlobalExceptionHandler returns HTTP 403
            // with a clean, user-facing message instead of a generic 401.
            throw new AccountDeactivatedException();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails == null
                ? List.of()
                : userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.isMustChangePassword());
    }
}
