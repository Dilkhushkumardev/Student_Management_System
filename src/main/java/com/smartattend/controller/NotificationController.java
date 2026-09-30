package com.smartattend.controller;

import com.smartattend.dto.AcademicDtoModels.NotificationDto;
import com.smartattend.dto.ApiResponse;
import com.smartattend.security.SecurityUtils;
import com.smartattend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Endpoints for user alerts, shortage warnings, and session notices")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @Operation(summary = "Get notifications for current authenticated user")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications() {
        Long userId = SecurityUtils.getCurrentUserId().orElse(1L);
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getUserNotifications(userId)));
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications for current authenticated user")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyUnreadNotifications() {
        Long userId = SecurityUtils.getCurrentUserId().orElse(1L);
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getUnreadNotifications(userId)));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.ok("Notification marked as read", "ID: " + id));
    }

    @PutMapping("/read-all")
    @Operation(summary = "Mark all notifications as read for current user")
    public ResponseEntity<ApiResponse<String>> markAllAsRead() {
        Long userId = SecurityUtils.getCurrentUserId().orElse(1L);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.ok("All notifications marked as read", "User ID: " + userId));
    }
}
