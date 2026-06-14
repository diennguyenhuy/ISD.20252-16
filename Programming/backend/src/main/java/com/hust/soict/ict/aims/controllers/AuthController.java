package com.hust.soict.ict.aims.controllers;

import com.hust.soict.ict.aims.dto.request.LoginRequest;
import com.hust.soict.ict.aims.dto.response.JwtResponse;
import com.hust.soict.ict.aims.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication entry point.
 *
 * Only exposes /api/auth/login.
 * Account creation is exclusively handled by administrators via /api/admin/users.
 * All business logic is delegated to {@link AuthService}.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}