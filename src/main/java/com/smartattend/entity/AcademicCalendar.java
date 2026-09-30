package com.smartattend.entity;

import com.smartattend.enums.EventType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "academic_calendar")
public class AcademicCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_title", nullable = false, length = 150)
    private String eventTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private EventType eventType = EventType.HOLIDAY;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays = 1;

    @Column(name = "day_name", length = 50)
    private String dayName;

    @Column(length = 255)
    private String description;

    @Column(name = "academic_session", nullable = false, length = 50)
    private String academicSession;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public AcademicCalendar() {}

    public AcademicCalendar(Long id, String eventTitle, EventType eventType, LocalDate startDate, LocalDate endDate, Integer totalDays, String dayName, String description, String academicSession, Boolean isActive) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.eventType = eventType != null ? eventType : EventType.HOLIDAY;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays != null ? totalDays : 1;
        this.dayName = dayName;
        this.description = description;
        this.academicSession = academicSession;
        this.isActive = isActive != null ? isActive : true;
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
        private EventType eventType = EventType.HOLIDAY;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer totalDays = 1;
        private String dayName;
        private String description;
        private String academicSession;
        private Boolean isActive = true;

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

        public AcademicCalendar build() {
            return new AcademicCalendar(id, eventTitle, eventType, startDate, endDate, totalDays, dayName, description, academicSession, isActive);
        }
    }
}
