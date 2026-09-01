package com.hust.soict.ict.aims.controllers.admin;

import com.hust.soict.ict.aims.dto.request.CreateUserRequest;
import com.hust.soict.ict.aims.dto.response.UserResponse;
import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.services.admin.AdminService;
import com.hust.soict.ict.aims.services.admin.UserQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

/**
 * REST controller for administrative user management.
 *
 * <p>All endpoints are secured to {@code ROLE_ADMINISTRATOR} via
 * {@link com.hust.soict.ict.aims.config.SecurityConfig}.
 *
 * <p>This controller is a thin HTTP adapter — all business logic
 * is delegated to {@link AdminService}.
 */
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final UserQueryService userQueryService;
    private final AdminService adminService;

    @GetMapping
    public Page<UserResponse> getUsers(@PageableDefault(size = 20) Pageable pageable) {
        return userQueryService.getUsers(pageable).map(UserResponse::new);
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable UUID userId) {
        return new UserResponse(userQueryService.getUser(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return new UserResponse(adminService.createUser(request));
    }

    @PatchMapping("/{userId}/deactivate")
    public UserResponse deactivateUser(@PathVariable UUID userId) {
        return new UserResponse(adminService.deactivateUser(userId));
    }

    @PatchMapping("/{userId}/activate")
    public UserResponse activateUser(@PathVariable UUID userId) {
        return new UserResponse(adminService.activateUser(userId));
    }

    @PatchMapping("/{userId}/block")
    public UserResponse blockUser(@PathVariable UUID userId) {
        return new UserResponse(adminService.blockUser(userId));
    }

    @PatchMapping("/{userId}/unblock")
    public UserResponse unblockUser(@PathVariable UUID userId) {
        return new UserResponse(adminService.unblockUser(userId));
    }

    @PutMapping("/{userId}/roles")
    public UserResponse assignRoles(
            @PathVariable UUID userId,
            @Valid @RequestBody
            @NotEmpty
            Set<User.@NotNull Role> newRoles
    ) {
        return new UserResponse(adminService.assignRoles(userId, newRoles));
    }

    @PostMapping("/{userId}/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@PathVariable UUID userId) {
        adminService.resetPassword(userId);
    }

    @PutMapping("/{userId}/email-update")
    public UserResponse updateEmail(
            @PathVariable UUID userId,
            @RequestParam(name = "newEmail")
            @NotBlank
            String newEmail
    ) {
        return new UserResponse(adminService.updateEmail(userId, newEmail));
    }
}
