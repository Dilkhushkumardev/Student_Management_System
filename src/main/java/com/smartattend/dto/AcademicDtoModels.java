package com.smartattend.dto;

import com.smartattend.enums.EventType;
import com.smartattend.enums.NotificationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AcademicDtoModels {

    public static class AcademicCalendarDto {
        private Long id;
        private String eventTitle;
        private EventType eventType;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer totalDays;
        private String dayName;
        private String description;
        private String academicSession;
        private Boolean isActive;

        public AcademicCalendarDto() {}

        public AcademicCalendarDto(Long id, String eventTitle, EventType eventType, LocalDate startDate,
                                   LocalDate endDate, Integer totalDays, String dayName, String description,
                                   String academicSession, Boolean isActive) {
            this.id = id;
            this.eventTitle = eventTitle;
            this.eventType = eventType;
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalDays = totalDays;
            this.dayName = dayName;
            this.description = description;
            this.academicSession = academicSession;
            this.isActive = isActive;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getEventTitle() { return eventTitle; }
        public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
        public EventType getEventType() { return eventType; }
        public void setEventType(EventType eventType) { this.eventType = eventType; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public Integer getTotalDays() { return totalDays; }
        public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; }
        public String getDayName() { return dayName; }
        public void setDayName(String dayName) { this.dayName = dayName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getAcademicSession() { return academicSession; }
        public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String eventTitle;
            private EventType eventType;
            private LocalDate startDate;
            private LocalDate endDate;
            private Integer totalDays;
            private String dayName;
            private String description;
            private String academicSession;
            private Boolean isActive;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder eventTitle(String eventTitle) { this.eventTitle = eventTitle; return this; }
            public Builder eventType(EventType eventType) { this.eventType = eventType; return this; }
            public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
            public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
            public Builder totalDays(Integer totalDays) { this.totalDays = totalDays; return this; }
            public Builder dayName(String dayName) { this.dayName = dayName; return this; }
            public Builder description(String description) { this.description = description; return this; }
            public Builder academicSession(String academicSession) { this.academicSession = academicSession; return this; }
            public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

            public AcademicCalendarDto build() {
                return new AcademicCalendarDto(id, eventTitle, eventType, startDate, endDate, totalDays, dayName, description, academicSession, isActive);
            }
        }
    }

    public static class AttendanceRuleDto {
        private Long id;
        private String ruleName;
        private BigDecimal minPercentageRequired;
        private BigDecimal condonationPercentageAllowed;
        private BigDecimal lateAsPresentWeight;
        private Boolean isActive;
        private String description;
        private LocalDateTime updatedAt;

        public AttendanceRuleDto() {}

        public AttendanceRuleDto(Long id, String ruleName, BigDecimal minPercentageRequired,
                                 BigDecimal condonationPercentageAllowed, BigDecimal lateAsPresentWeight,
                                 Boolean isActive, String description, LocalDateTime updatedAt) {
            this.id = id;
            this.ruleName = ruleName;
            this.minPercentageRequired = minPercentageRequired;
            this.condonationPercentageAllowed = condonationPercentageAllowed;
            this.lateAsPresentWeight = lateAsPresentWeight;
            this.isActive = isActive;
            this.description = description;
            this.updatedAt = updatedAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }
        public BigDecimal getMinPercentageRequired() { return minPercentageRequired; }
        public void setMinPercentageRequired(BigDecimal minPercentageRequired) { this.minPercentageRequired = minPercentageRequired; }
        public BigDecimal getCondonationPercentageAllowed() { return condonationPercentageAllowed; }
        public void setCondonationPercentageAllowed(BigDecimal condonationPercentageAllowed) { this.condonationPercentageAllowed = condonationPercentageAllowed; }
        public BigDecimal getLateAsPresentWeight() { return lateAsPresentWeight; }
        public void setLateAsPresentWeight(BigDecimal lateAsPresentWeight) { this.lateAsPresentWeight = lateAsPresentWeight; }
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String ruleName;
            private BigDecimal minPercentageRequired;
            private BigDecimal condonationPercentageAllowed;
            private BigDecimal lateAsPresentWeight;
            private Boolean isActive;
            private String description;
            private LocalDateTime updatedAt;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder ruleName(String ruleName) { this.ruleName = ruleName; return this; }
            public Builder minPercentageRequired(BigDecimal minPercentageRequired) { this.minPercentageRequired = minPercentageRequired; return this; }
            public Builder condonationPercentageAllowed(BigDecimal condonationPercentageAllowed) { this.condonationPercentageAllowed = condonationPercentageAllowed; return this; }
            public Builder lateAsPresentWeight(BigDecimal lateAsPresentWeight) { this.lateAsPresentWeight = lateAsPresentWeight; return this; }
            public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
            public Builder description(String description) { this.description = description; return this; }
            public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public AttendanceRuleDto build() {
                return new AttendanceRuleDto(id, ruleName, minPercentageRequired, condonationPercentageAllowed, lateAsPresentWeight, isActive, description, updatedAt);
            }
        }
    }

    public static class AttendanceMarkRuleDto {
        private Long id;
        private BigDecimal minPercentage;
        private BigDecimal maxPercentage;
        private Integer marksAwarded;
        private String ruleDescription;
        private Boolean isActive;

        public AttendanceMarkRuleDto() {}

        public AttendanceMarkRuleDto(Long id, BigDecimal minPercentage, BigDecimal maxPercentage,
                                     Integer marksAwarded, String ruleDescription, Boolean isActive) {
            this.id = id;
            this.minPercentage = minPercentage;
            this.maxPercentage = maxPercentage;
            this.marksAwarded = marksAwarded;
            this.ruleDescription = ruleDescription;
            this.isActive = isActive;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public BigDecimal getMinPercentage() { return minPercentage; }
        public void setMinPercentage(BigDecimal minPercentage) { this.minPercentage = minPercentage; }
        public BigDecimal getMaxPercentage() { return maxPercentage; }
        public void setMaxPercentage(BigDecimal maxPercentage) { this.maxPercentage = maxPercentage; }
        public Integer getMarksAwarded() { return marksAwarded; }
        public void setMarksAwarded(Integer marksAwarded) { this.marksAwarded = marksAwarded; }
        public String getRuleDescription() { return ruleDescription; }
        public void setRuleDescription(String ruleDescription) { this.ruleDescription = ruleDescription; }
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private BigDecimal minPercentage;
            private BigDecimal maxPercentage;
            private Integer marksAwarded;
            private String ruleDescription;
            private Boolean isActive;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder minPercentage(BigDecimal minPercentage) { this.minPercentage = minPercentage; return this; }
            public Builder maxPercentage(BigDecimal maxPercentage) { this.maxPercentage = maxPercentage; return this; }
            public Builder marksAwarded(Integer marksAwarded) { this.marksAwarded = marksAwarded; return this; }
            public Builder ruleDescription(String ruleDescription) { this.ruleDescription = ruleDescription; return this; }
            public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

            public AttendanceMarkRuleDto build() {
                return new AttendanceMarkRuleDto(id, minPercentage, maxPercentage, marksAwarded, ruleDescription, isActive);
            }
        }
    }

    public static class AuditLogDto {
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

        public AuditLogDto() {}

        public AuditLogDto(Long id, Long userId, String username, String userRole, String action,
                           String entityName, String entityId, String oldValue, String newValue,
                           String ipAddress, String details, LocalDateTime timestamp) {
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

            public AuditLogDto build() {
                return new AuditLogDto(id, userId, username, userRole, action, entityName, entityId, oldValue, newValue, ipAddress, details, timestamp);
            }
        }
    }

    public static class NotificationDto {
        private Long id;
        private Long userId;
        private String title;
        private String message;
        private NotificationType notificationType;
        private Boolean isRead;
        private String referenceId;
        private LocalDateTime createdAt;

        public NotificationDto() {}

        public NotificationDto(Long id, Long userId, String title, String message,
                               NotificationType notificationType, Boolean isRead,
                               String referenceId, LocalDateTime createdAt) {
            this.id = id;
            this.userId = userId;
            this.title = title;
            this.message = message;
            this.notificationType = notificationType;
            this.isRead = isRead;
            this.referenceId = referenceId;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
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
            private Long userId;
            private String title;
            private String message;
            private NotificationType notificationType;
            private Boolean isRead;
            private String referenceId;
            private LocalDateTime createdAt;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder userId(Long userId) { this.userId = userId; return this; }
            public Builder title(String title) { this.title = title; return this; }
            public Builder message(String message) { this.message = message; return this; }
            public Builder notificationType(NotificationType notificationType) { this.notificationType = notificationType; return this; }
            public Builder isRead(Boolean isRead) { this.isRead = isRead; return this; }
            public Builder referenceId(String referenceId) { this.referenceId = referenceId; return this; }
            public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public NotificationDto build() {
                return new NotificationDto(id, userId, title, message, notificationType, isRead, referenceId, createdAt);
            }
        }
    }
}
