package com.hust.soict.ict.aims.models.entities.user;

import com.hust.soict.ict.aims.exceptions.ValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Getter(AccessLevel.NONE)
    private String hashedPassword;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Set<UserRole> roles = new HashSet<>();

    public User(Builder builder) throws ValidationException {
        UserValidator.validateUsername(builder.username);
        UserValidator.validateEmail(builder.username);
        UserValidator.validateHashedPassword(builder.hashedPassword);
        UserValidator.validateRoles(builder.roles);

        this.username = builder.username;
        this.email = builder.email;
        this.hashedPassword = builder.hashedPassword;
        this.roles = new HashSet<>(builder.roles);
    }

    public static class Builder {
        private String username;
        private String email;
        private String hashedPassword;
        private Set<UserRole> roles = new HashSet<>();

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder hashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }

        public Builder roles(Set<UserRole> roles) {
            this.roles = roles;
            return this;
        }

        public User build() throws ValidationException {
            return new User(this);
        }
    }
}

final class UserValidator {
    private UserValidator() {}

    static void validateUsername(String username) throws ValidationException {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required", "username");
        }
    }

    static void validateEmail(String email) throws ValidationException {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required", "email");
        }
    }

    static void validateHashedPassword(String hashedPassword) throws ValidationException {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new ValidationException("Hashed password is required", "hashedPassword");
        }
    }

    static void validateRoles(Set<UserRole> roles) throws ValidationException {
        if (roles == null || roles.isEmpty()) {
            throw new ValidationException("Roles are required", "roles");
        }
    }
}
