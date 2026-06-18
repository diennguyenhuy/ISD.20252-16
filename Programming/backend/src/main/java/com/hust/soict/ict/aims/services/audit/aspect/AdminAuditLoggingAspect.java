package com.hust.soict.ict.aims.services.audit.aspect;

import com.hust.soict.ict.aims.dto.request.CreateUserRequest;
import com.hust.soict.ict.aims.exceptions.AccountNotFoundException;
import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import com.hust.soict.ict.aims.models.entities.audit.UserAction;
import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.repositories.AdminLogRepository;
import com.hust.soict.ict.aims.repositories.UserRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import com.hust.soict.ict.aims.services.audit.AdminLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Aspect @Component
@RequiredArgsConstructor
@Slf4j
public class AdminAuditLoggingAspect {
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final AdminLogRepository adminLogRepository;

    @AfterReturning(
            value = "@annotation(adminLogging) && args(request)",
            argNames = "adminLogging,request"
    )
    public void logUserCreation(AdminLogging adminLogging, CreateUserRequest request) {
        if (adminLogging.action() != UserAction.CREATE) {
            return;
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AccountNotFoundException("User with email " + request.getEmail() + " not found"));

        AdminLog adminLog = new AdminLog(
                authenticationFacade.getCurrentUser(),
                user,
                adminLogging.action()
        );
        adminLogRepository.save(adminLog);
    }

    @AfterReturning(
            value = "@annotation(adminLogging) && args(userId)",
            argNames = "adminLogging,userId"
    )
    public void logUserActivationOrBlocking(AdminLogging adminLogging, UUID userId) {
        if (adminLogging.action() != UserAction.DEACTIVATE
        && adminLogging.action() != UserAction.ACTIVATE
        && adminLogging.action() != UserAction.BLOCK
        && adminLogging.action() != UserAction.UNBLOCK
        && adminLogging.action() != UserAction.RESET_PASSWORD) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = new AdminLog(
                authenticationFacade.getCurrentUser(),
                user,
                adminLogging.action()
        );
        adminLogRepository.save(adminLog);
    }

    @AfterReturning(
            value = "@annotation(adminLogging) && args(userId,newRoles)",
            argNames = "adminLogging,userId,newRoles"
    )
    public void logUserAssignOrModifyRoles(AdminLogging adminLogging, UUID userId, Set<User.Role> newRoles) {
        if (adminLogging.action() != UserAction.MODIFY_ROLE) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = new AdminLog(
                authenticationFacade.getCurrentUser(),
                user,
                adminLogging.action()
        );
        adminLogRepository.save(adminLog);
    }

    @AfterReturning(
            value = "@annotation(adminLogging) && args(userId,newEmail)",
            argNames = "adminLogging,userId,newEmail"
    )
    public void logUserEmailUpdate(AdminLogging adminLogging, UUID userId, String newEmail) {
        if (adminLogging.action() != UserAction.UPDATE_EMAIL) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User with id " + userId + " not found"));

        AdminLog adminLog = new AdminLog(
                authenticationFacade.getCurrentUser(),
                user,
                adminLogging.action()
        );
        adminLogRepository.save(adminLog);
    }
}
