package com.hust.soict.ict.aims.models.entities.user;

import com.hust.soict.ict.aims.models.entities.VersionedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "\"user\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends VersionedEntity {
    public enum Role {
        PRODUCT_MANAGER,
        ADMINISTRATOR,
    }

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    @NonNull @Setter
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    private boolean active = true;
    private boolean blocked = false;

    private boolean mustChangePassword = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Set<Role> roles = new HashSet<>();

    public void addRole(@NonNull Role role) {
        this.roles.add(role);
    }

    public void removeRole(@NonNull Role role) {
        this.roles.remove(role);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(this.roles);
    }

    public void activate() {
        if (this.active) {
            throw new IllegalStateException("User " + username + " is already active.");
        }
        this.active = true;
    }

    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("User " + username + " is already deactivated.");
        }
        this.active = false;
    }

    public void block() {
        if (this.blocked) {
            throw new IllegalStateException("User " + username + " is already blocked.");
        }
        this.blocked = true;
    }

    public void unblock() {
        if (!this.blocked) {
            throw new IllegalStateException("User " + username + " is already unblocked.");
        }
        this.blocked = false;
    }

    /**
     * Updates the user's hashed password and clears the mustChangePassword flag.
     * This is the ONLY way to change a password through the domain model.
     */
    public void updatePassword(@NonNull String newHashedPassword) {
        this.hashedPassword = newHashedPassword;
        this.mustChangePassword = false;
    }

    /**
     * Called by an admin to force the user to change their password on next login.
     * Sets a temporary hashed password and raises the mustChangePassword flag.
     */
    public void setTemporaryPassword(@NonNull String temporaryHashedPassword) {
        this.hashedPassword = temporaryHashedPassword;
        this.mustChangePassword = true;
    }

    public User(String username, String email, String hashedPassword, Set<Role> roles) {
        this.username = Objects.requireNonNull(username, "User name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.hashedPassword = Objects.requireNonNull(hashedPassword, "Hashed password cannot be null");
        this.roles = new HashSet<>(Objects.requireNonNull(roles, "Roles cannot be null"));
    }
}
