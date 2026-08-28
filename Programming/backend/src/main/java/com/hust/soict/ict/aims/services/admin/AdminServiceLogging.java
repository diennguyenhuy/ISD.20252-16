package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.dto.request.CreateUserRequest;
import com.hust.soict.ict.aims.exceptions.AccountNotFoundException;
import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import com.hust.soict.ict.aims.models.entities.audit.UserAction;
import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.repositories.AdminLogRepository;
import com.hust.soict.ict.aims.repositories.UserRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
class AdminServiceLogging implements AdminService {
    private final AdminService adminService;

    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final AdminLogRepository adminLogRepository;

    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        var user = adminService.createUser(request);

        AdminLog adminLog = UserAction.CREATE.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return user;
    }

    @Override
    @Transactional
    public User deactivateUser(UUID userId) {
        var user = adminService.deactivateUser(userId);

        AdminLog adminLog = UserAction.DEACTIVATE.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return user;
    }

    @Override
    @Transactional
    public User activateUser(UUID userId) {
        var user = adminService.activateUser(userId);

        AdminLog adminLog = UserAction.ACTIVATE.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return user;
    }

    @Override
    @Transactional
    public User blockUser(UUID userId) {
        var user = adminService.blockUser(userId);

        AdminLog adminLog = UserAction.BLOCK.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return user;
    }

    @Override
    @Transactional
    public User unblockUser(UUID userId) {
        var user = adminService.unblockUser(userId);

        AdminLog adminLog = UserAction.UNBLOCK.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return user;
    }

    @Override
    @Transactional
    public User assignRoles(UUID userId, Set<User.Role> newRoles) {
        var user = adminService.assignRoles(userId, newRoles);

        AdminLog adminLog = UserAction.MODIFY_ROLE.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return user;
    }

    @Override
    @Transactional
    public void resetPassword(UUID userId) {
        adminService.resetPassword(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = UserAction.RESET_PASSWORD.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);
    }

    @Override
    @Transactional
    public User updateEmail(UUID userId, String newEmail) {
        var user = adminService.updateEmail(userId, newEmail);

        AdminLog adminLog = UserAction.UPDATE_EMAIL.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return user;
    }
}

@Configuration
class AdminConfiguration {
    @Primary @Bean
    public AdminService adminService(
            AdminServiceImpl adminService,
            UserRepository userRepository,
            AuthenticationFacade authenticationFacade,
            AdminLogRepository adminLogRepository
    ) {
        return new AdminServiceLogging(
                adminService,
                userRepository,
                authenticationFacade,
                adminLogRepository
        );
    }
}
