package com.hust.soict.ict.aims.controllers.admin;

import com.hust.soict.ict.aims.dto.request.CreateUserRequest;
import com.hust.soict.ict.aims.dto.response.UserResponse;
import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.services.admin.AdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(adminService.getUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse created = adminService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable UUID id) {
        adminService.deactivateUser(id);
        return ResponseEntity.ok(Map.of("message", "User has been deactivated."));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Map<String, String>> activateUser(@PathVariable UUID id) {
        adminService.activateUser(id);
        return ResponseEntity.ok(Map.of("message", "User has been activated."));
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<Map<String, String>> blockUser(@PathVariable UUID id) {
        adminService.blockUser(id);
        return ResponseEntity.ok(Map.of("message", "User has been blocked."));
    }

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<Map<String, String>> unblockUser(@PathVariable UUID id) {
        adminService.unblockUser(id);
        return ResponseEntity.ok(Map.of("message", "User has been unblocked."));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserResponse> assignRoles(
            @PathVariable UUID id,
            @RequestBody
            @NotEmpty
            Set<User.@NotNull Role> newRoles
    ) {
        return ResponseEntity.ok(adminService.assignRoles(id, newRoles));
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable UUID id) {
        adminService.resetPassword(id);
        return ResponseEntity.ok(Map.of("message",
                "Password has been reset. A temporary password has been sent to the user's email."));
    }

    @PutMapping("/{id}/email-update")
    public ResponseEntity<Map<String, String>> updateEmail(
            @PathVariable UUID id,
            @RequestBody
            @NotBlank
            String newEmail
    ) {
        adminService.updateEmail(id, newEmail);
        return ResponseEntity.ok(Map.of("message", "Email has been updated."));
    }
}
