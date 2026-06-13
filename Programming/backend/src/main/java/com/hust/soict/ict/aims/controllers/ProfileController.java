package com.hust.soict.ict.aims.controllers;

import com.hust.soict.ict.aims.dto.request.ChangePasswordRequest;
import com.hust.soict.ict.aims.security.services.UserDetailsImpl;
import com.hust.soict.ict.aims.services.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Self-service profile endpoints accessible to all authenticated users.
 *
 * <p>The password-change endpoint is whitelisted in the JWT filter so that
 * users with {@code mustChangePassword = true} can reach it.
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(principal.getId(), request);
        return ResponseEntity.ok(Map.of("message",
                "Password changed successfully. Please log in again with your new password."));
    }
}
