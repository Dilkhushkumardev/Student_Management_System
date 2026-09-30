package com.smartattend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    public static Optional<CustomUserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(CustomUserDetails::getId);
    }

    public static Optional<String> getCurrentUsername() {
        return getCurrentUser().map(CustomUserDetails::getUsername);
    }
}
