package com.smartattend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(name = "user_role", length = 50)
    private String userRole;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(name = "entity_name", nullable = false, length = 100)
    private String entityName;

    @Column(name = "entity_id", length = 50)
    private String entityId;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String details;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(Long id, Long userId, String username, String userRole, String action, String entityName, String entityId, String oldValue, String newValue, String ipAddress, String details, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.userRole = userRole;
        this.action = action;
        this.entityName = entityName;
        this.entityId = entityId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
        this.details = details;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long userId;
        private String username;
        private String userRole;
        private String action;
        private String entityName;
        private String entityId;
        private String oldValue;
        private String newValue;
        private String ipAddress;
        private String details;
        private LocalDateTime timestamp;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder userRole(String userRole) { this.userRole = userRole; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder entityName(String entityName) { this.entityName = entityName; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }
        public Builder oldValue(String oldValue) { this.oldValue = oldValue; return this; }
        public Builder newValue(String newValue) { this.newValue = newValue; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder details(String details) { this.details = details; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public AuditLog build() {
            return new AuditLog(id, userId, username, userRole, action, entityName, entityId, oldValue, newValue, ipAddress, details, timestamp);
        }
    }
}
