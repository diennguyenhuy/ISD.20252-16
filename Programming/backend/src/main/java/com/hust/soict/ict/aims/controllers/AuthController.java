package com.hust.soict.ict.aims.controllers;

import com.hust.soict.ict.aims.dto.request.LoginRequest;
import com.hust.soict.ict.aims.dto.request.SignupRequest;
import com.hust.soict.ict.aims.dto.response.JwtResponse;
import com.hust.soict.ict.aims.models.entities.user.User;
import com.hust.soict.ict.aims.repositories.UserRepository;
import com.hust.soict.ict.aims.security.jwt.JwtUtils;
import com.hust.soict.ict.aims.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Khắc phục cảnh báo NullPointerException
        List<String> roles = new ArrayList<>();
        if (userDetails.getAuthorities() != null) {
            roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Gán role mặc định là PRODUCT_MANAGER cho các tài khoản đăng ký mới
        User user = new User.Builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .hashedPassword(encoder.encode(signUpRequest.getPassword()))
                .roles(Set.of(User.Role.PRODUCT_MANAGER))
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}