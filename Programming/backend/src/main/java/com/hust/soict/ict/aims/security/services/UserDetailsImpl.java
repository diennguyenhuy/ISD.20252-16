package com.hust.soict.ict.aims.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.soict.ict.aims.models.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private UUID id;
    private String username;
    private String email;

    @JsonIgnore // Không bao giờ cho phép lộ password ra JSON
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    /** Mirrors User.isActive(). False → Spring Security rejects login with DisabledException. */
    private boolean active;

    /** Mirrors User.isBlocked(). Exposed so service layer can check without re-querying DB. */
    private boolean blocked;

    /** True when an admin has triggered a password reset for this user. */
    private boolean mustChangePassword;

    // --- Factory method ---

    /** Converts a User entity into a UserDetailsImpl for the security context. */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getHashedPassword(),
                authorities,
                user.isActive(),
                user.isBlocked(),
                user.isMustChangePassword());
    }

    // --- UserDetails contract ---

    /**
     * Returns false for deactivated accounts.
     * Spring Security will throw DisabledException during authentication,
     * which we map to HTTP 403 in the GlobalExceptionHandler.
     */
    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    /**
     * Blocked users CAN still authenticate (isAccountNonLocked = true).
     * The blocked check is enforced at the service layer, not here.
     */
    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }
}