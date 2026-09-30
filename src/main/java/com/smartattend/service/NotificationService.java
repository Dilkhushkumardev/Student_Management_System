package com.smartattend.service;

import com.smartattend.dto.AcademicDtoModels.NotificationDto;
import com.smartattend.entity.Notification;
import com.smartattend.enums.NotificationType;
import com.smartattend.repository.NotificationRepository;
import com.smartattend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void sendNotification(Long userId, String title, String message, NotificationType type, String referenceId) {
        userRepository.findById(userId).ifPresent(user -> {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setNotificationType(type);
            notification.setReferenceId(referenceId);
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);
            log.info("NOTIFICATION sent to user {}: [{}] {}", user.getUsername(), title, message);
        });
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> list = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        for (Notification n : list) {
            n.setIsRead(true);
        }
        notificationRepository.saveAll(list);
    }

    private NotificationDto mapToDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .userId(n.getUser() != null ? n.getUser().getId() : null)
                .title(n.getTitle())
                .message(n.getMessage())
                .notificationType(n.getNotificationType())
                .isRead(n.getIsRead())
                .referenceId(n.getReferenceId())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
