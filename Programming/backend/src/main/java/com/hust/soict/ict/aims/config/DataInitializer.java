package com.hust.soict.ict.aims.config;

import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Seeds the initial administrator account on first boot.
 *
 * <p>Runs once at application startup. If the configured admin email already
 * exists in the database, this is a no-op — safe for repeated restarts.
 *
 * <p>Credentials are sourced from {@code application.yaml} with environment
 * variable overrides for production:
 * <pre>
 *   aims.admin.email           → AIMS_ADMIN_EMAIL
 *   aims.admin.username        → AIMS_ADMIN_USERNAME
 *   aims.admin.initial-password → ADMIN_INITIAL_PASSWORD
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${aims.admin.email}")
    private String adminEmail;

    @Value("${aims.admin.username}")
    private String adminUsername;

    @Value("${aims.admin.initial-password}")
    private String adminInitialPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin account ({}) already exists — skipping seed.", adminEmail);
            return;
        }

        User admin = new User(
                adminUsername,
                adminEmail,
                passwordEncoder.encode(adminInitialPassword),
                Set.of(User.Role.ADMINISTRATOR, User.Role.PRODUCT_MANAGER));

        userRepository.save(admin);
        log.info("🔑 Seeded initial admin account: {} ({}) with roles [ADMINISTRATOR, PRODUCT_MANAGER]",
                adminUsername, adminEmail);
    }
}
