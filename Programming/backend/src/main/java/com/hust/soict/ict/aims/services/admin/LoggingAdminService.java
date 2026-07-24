package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.dto.request.CreateUserRequest;
import com.hust.soict.ict.aims.dto.response.UserResponse;
import com.hust.soict.ict.aims.exceptions.AccountNotFoundException;
import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import com.hust.soict.ict.aims.models.entities.audit.UserAction;
import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.repositories.AdminLogRepository;
import com.hust.soict.ict.aims.repositories.UserRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
class LoggingAdminService implements AdminService {
    private final AdminService adminService;

    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final AdminLogRepository adminLogRepository;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        var result = adminService.createUser(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AccountNotFoundException("User with email " + request.getEmail() + " not found"));

        AdminLog adminLog = UserAction.CREATE.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return result;
    }

    @Override
    @Transactional
    public void deactivateUser(UUID userId) {
        adminService.deactivateUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = UserAction.DEACTIVATE.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);
    }

    @Override
    @Transactional
    public void activateUser(UUID userId) {
        adminService.activateUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = UserAction.ACTIVATE.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);
    }

    @Override
    @Transactional
    public void blockUser(UUID userId) {
        adminService.blockUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = UserAction.BLOCK.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);
    }

    @Override
    @Transactional
    public void unblockUser(UUID userId) {
        adminService.unblockUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = UserAction.UNBLOCK.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);
    }

    @Override
    @Transactional
    public UserResponse assignRoles(UUID userId, Set<User.Role> newRoles) {
        var result = adminService.assignRoles(userId, newRoles);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = UserAction.MODIFY_ROLE.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);

        return result;
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
    public void updateEmail(UUID userId, String newEmail) {
        adminService.updateEmail(userId, newEmail);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = UserAction.UPDATE_EMAIL.log(authenticationFacade.getCurrentUser(), user);
        adminLogRepository.save(adminLog);
    }
}
