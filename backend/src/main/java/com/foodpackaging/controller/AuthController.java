package com.foodpackaging.controller;

import com.foodpackaging.dto.ApiResponse;
import com.foodpackaging.dto.AuthResponse;
import com.foodpackaging.dto.LoginRequest;
import com.foodpackaging.dto.RegisterRequest;
import com.foodpackaging.entity.User;
import com.foodpackaging.security.UserPrincipal;
import com.foodpackaging.service.AuthService;
import com.foodpackaging.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response, LocalDateTime.now().toString()));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registration successful", response, LocalDateTime.now().toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Not authenticated", null, LocalDateTime.now().toString()));
        }
        User user = userService.getUserById(principal.getId());
        AuthResponse response = AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .token("")
                .type("Bearer")
                .build();
        return ResponseEntity.ok(new ApiResponse<>(true, "User profile retrieved", response, LocalDateTime.now().toString()));
    }
}
