package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.dto.request.CreateUserRequest;
import com.hust.soict.ict.aims.dto.response.UserResponse;
import com.hust.soict.ict.aims.models.entities.user.User;

import java.util.Set;
import java.util.UUID;

public interface AdminService {
    UserResponse createUser(CreateUserRequest request);
    void deactivateUser(UUID userId);
    void activateUser(UUID userId);
    void blockUser(UUID userId);
    void unblockUser(UUID userId);
    UserResponse assignRoles(UUID userId, Set<User.Role> newRoles);
    void resetPassword(UUID userId);
    void updateEmail(UUID userId, String newEmail);

}
