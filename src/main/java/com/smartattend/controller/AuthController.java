package com.smartattend.controller;

import com.smartattend.dto.ApiResponse;
import com.smartattend.dto.AuthDto.*;
import com.smartattend.security.SecurityUtils;
import com.smartattend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication, JWT tokens, and session management")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get JWT token")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT access token using refresh token")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.ok("Token refreshed successfully", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user and invalidate refresh tokens")
    public ResponseEntity<ApiResponse<String>> logoutUser() {
        SecurityUtils.getCurrentUserId().ifPresent(authService::logout);
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully", "Session terminated"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user profile")
    public ResponseEntity<ApiResponse<Object>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponse.ok(SecurityUtils.getCurrentUser().orElse(null)));
    }
}
