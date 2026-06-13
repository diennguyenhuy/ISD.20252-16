package com.hust.soict.ict.aims.services.profile;

import com.hust.soict.ict.aims.dto.request.ChangePasswordRequest;
import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.repositories.UserRepository;
import com.hust.soict.ict.aims.services.notification.NotificationService;
import com.hust.soict.ict.aims.services.notification.email.ProfileUpdateEmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Self-service profile operations available to all authenticated users.
 *
 * <p>Currently supports password change only. This is the single endpoint
 * allowed for users with {@code mustChangePassword = true} (enforced by the JWT filter).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    /**
     * Changes the authenticated user's password.
     *
     * <p>Steps:
     * <ol>
     *     <li>Verify the current password matches.</li>
     *     <li>Verify new password and confirmation match.</li>
     *     <li>Hash and persist the new password (clears {@code mustChangePassword}).</li>
     *     <li>Send an audit notification email to the user.</li>
     * </ol>
     *
     * @param userId  the ID of the authenticated user (from SecurityContext)
     * @param request the password change payload
     */
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        // 1. Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getHashedPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // 2. Verify new password and confirmation match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match.");
        }

        // 3. Verify new password differs from current
        if (passwordEncoder.matches(request.getNewPassword(), user.getHashedPassword())) {
            throw new IllegalArgumentException("New password must be different from the current password.");
        }

        // 4. Hash and persist — updatePassword() also clears mustChangePassword
        String hashedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.updatePassword(hashedNewPassword);
        userRepository.save(user);

        // 5. Send audit email
        notificationService.send(
                ProfileUpdateEmailMessage.class,
                new ProfileUpdateEmailMessage.Payload(
                        user.getEmail(),
                        user.getUsername(),
                        "Password Changed"));

        log.info("User {} ({}) changed their password.", user.getUsername(), user.getEmail());
    }
}
