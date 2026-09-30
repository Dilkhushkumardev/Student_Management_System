package com.smartattend.entity;

import com.smartattend.enums.NotificationType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 50)
    private NotificationType notificationType = NotificationType.SYSTEM;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "reference_id", length = 100)
    private String referenceId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(Long id, User user, String title, String message, NotificationType notificationType, Boolean isRead, String referenceId, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.message = message;
        this.notificationType = notificationType != null ? notificationType : NotificationType.SYSTEM;
        this.isRead = isRead != null ? isRead : false;
        this.referenceId = referenceId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public NotificationType getNotificationType() { return notificationType; }
    public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String title;
        private String message;
        private NotificationType notificationType = NotificationType.SYSTEM;
        private Boolean isRead = false;
        private String referenceId;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder notificationType(NotificationType notificationType) { this.notificationType = notificationType; return this; }
        public Builder isRead(Boolean isRead) { this.isRead = isRead; return this; }
        public Builder referenceId(String referenceId) { this.referenceId = referenceId; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Notification build() {
            return new Notification(id, user, title, message, notificationType, isRead, referenceId, createdAt);
        }
    }
}
