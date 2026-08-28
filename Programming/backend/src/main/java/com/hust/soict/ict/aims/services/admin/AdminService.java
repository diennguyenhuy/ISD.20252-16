package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.dto.request.CreateUserRequest;
import com.hust.soict.ict.aims.models.entities.user.User;

import java.util.Set;
import java.util.UUID;

public interface AdminService {
    User createUser(CreateUserRequest request);
    User deactivateUser(UUID userId);
    User activateUser(UUID userId);
    User blockUser(UUID userId);
    User unblockUser(UUID userId);
    User assignRoles(UUID userId, Set<User.Role> newRoles);
    void resetPassword(UUID userId);
    User updateEmail(UUID userId, String newEmail);
}
