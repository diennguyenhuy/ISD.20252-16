package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.dto.request.CreateUserRequest;
import com.hust.soict.ict.aims.dto.response.UserResponse;
import com.hust.soict.ict.aims.exceptions.AccountAlreadyExistedException;
import com.hust.soict.ict.aims.models.entities.audit.UserAction;
import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.repositories.UserRepository;
import com.hust.soict.ict.aims.services.admin.event.AccountCreatedEvent;
import com.hust.soict.ict.aims.services.admin.event.EmailUpdateEvent;
import com.hust.soict.ict.aims.services.admin.event.PasswordResetEvent;
import com.hust.soict.ict.aims.services.audit.aspect.AdminLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

/**
 * Centralized administration service.
 *
 * <p>Responsibilities:
 * <ul>
 *     <li>Create new user accounts (admin-only; no self-registration).</li>
 *     <li>Activate / deactivate / block / unblock accounts.</li>
 *     <li>Assign roles to users.</li>
 *     <li>Trigger password resets (admin never sees the plaintext password).</li>
 * </ul>
 *
 * <p>All password resets generate a secure temporary password, hash it, persist it,
 * and send the plaintext to the user via the team's notification framework.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String PASSWORD_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
    private static final int TEMP_PASSWORD_LENGTH = 12;

    // ───── Queries ─────

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        return UserResponse.from(findUserOrThrow(userId));
    }

    // ───── Account Lifecycle ─────

    @AdminLogging(action = UserAction.CREATE)
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AccountAlreadyExistedException("Email is already in use: " + request.getEmail());
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AccountAlreadyExistedException("Username is already in use: " + request.getUsername());
        }

        // Generate a temporary password — admin never sees it
        String tempPassword = generateTemporaryPassword();
        String hashedPassword = passwordEncoder.encode(tempPassword);

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                hashedPassword,
                request.getRoles());

        // Force the user to change password on first login
        user.setTemporaryPassword(hashedPassword);

        userRepository.save(user);

        // Send the temporary password to the user via email
        applicationEventPublisher.publishEvent(new AccountCreatedEvent(user, tempPassword));

        log.info("Admin created new user: {} ({})", user.getUsername(), user.getEmail());
        return UserResponse.from(user);
    }

    @AdminLogging(action = UserAction.DEACTIVATE)
    @Transactional
    public void deactivateUser(UUID userId) {
        User user = findUserOrThrow(userId);
        user.deactivate();
        userRepository.save(user);
        log.info("Admin deactivated user: {} ({})", user.getUsername(), user.getEmail());
    }

    @AdminLogging(action = UserAction.ACTIVATE)
    @Transactional
    public void activateUser(UUID userId) {
        User user = findUserOrThrow(userId);
        user.activate();
        userRepository.save(user);
        log.info("Admin activated user: {} ({})", user.getUsername(), user.getEmail());
    }

    @AdminLogging(action = UserAction.BLOCK)
    @Transactional
    public void blockUser(UUID userId) {
        User user = findUserOrThrow(userId);
        user.block();
        userRepository.save(user);
        log.info("Admin blocked user: {} ({})", user.getUsername(), user.getEmail());
    }

    @AdminLogging(action = UserAction.UNBLOCK)
    @Transactional
    public void unblockUser(UUID userId) {
        User user = findUserOrThrow(userId);
        user.unblock();
        userRepository.save(user);
        log.info("Admin unblocked user: {} ({})", user.getUsername(), user.getEmail());
    }

    // ───── Roles ─────

    @AdminLogging(action = UserAction.MODIFY_ROLE)
    @Transactional
    public UserResponse assignRoles(UUID userId, Set<User.Role> newRoles) {
        User user = findUserOrThrow(userId);

        Set<User.Role> currentRoles = user.getRoles();

        Set<User.Role> toAdd = new HashSet<>(newRoles);
        toAdd.removeAll(currentRoles);
        Set<User.Role> toRemove = new HashSet<>(currentRoles);
        toRemove.removeAll(newRoles);

        toRemove.forEach(user::removeRole);
        toAdd.forEach(user::addRole);

        userRepository.save(user);
        log.info("Admin updated roles for user {} to {}", user.getUsername(), newRoles);
        return UserResponse.from(user);
    }

    // ───── Password Reset ─────

    /**
     * Generates a secure temporary password, hashes it, stores it, and sends the
     * plaintext to the user via email. The admin NEVER sees the password.
     *
     * <p>Sets {@code mustChangePassword = true} so the user is forced to change it
     * on their next login (enforced by the JWT filter).
     */
    @AdminLogging(action = UserAction.RESET_PASSWORD)
    @Transactional
    public void resetPassword(UUID userId) {
        User user = findUserOrThrow(userId);

        String tempPassword = generateTemporaryPassword();
        String hashedPassword = passwordEncoder.encode(tempPassword);
        user.setTemporaryPassword(hashedPassword);
        userRepository.save(user);

        applicationEventPublisher.publishEvent(new PasswordResetEvent(user, tempPassword));

        log.info("Admin triggered password reset for user: {} ({})", user.getUsername(), user.getEmail());
    }

    @AdminLogging(action = UserAction.UPDATE_EMAIL)
    @Transactional
    public void updateEmail(UUID userId, String newEmail) {
        if (userRepository.existsByEmail(newEmail)) {
            throw new AccountAlreadyExistedException("Email is already in use: " + newEmail);
        }

        User user = findUserOrThrow(userId);

        String oldEmail = user.getEmail();

        user.setEmail(newEmail);
        userRepository.save(user);

        applicationEventPublisher.publishEvent(new EmailUpdateEvent(user, oldEmail, newEmail));

        log.info("Admin updated email for user: {} ({})", user.getUsername(), user.getEmail());
    }

    // ───── Helpers ─────

    private User findUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
    }

    private String generateTemporaryPassword() {
        StringBuilder sb = new StringBuilder(TEMP_PASSWORD_LENGTH);
        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            sb.append(PASSWORD_CHARS.charAt(SECURE_RANDOM.nextInt(PASSWORD_CHARS.length())));
        }
        return sb.toString();
    }
}
