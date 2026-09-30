package com.smartattend.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "timetable")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_of_week", nullable = false, length = 20)
    private String dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "period_number", nullable = false)
    private Integer periodNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(name = "room_no", nullable = false, length = 50)
    private String roomNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "academic_session", nullable = false, length = 50)
    private String academicSession;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Timetable() {}

    public Timetable(Long id, String dayOfWeek, LocalTime startTime, LocalTime endTime, Integer periodNumber, Subject subject, Faculty faculty, String roomNo, Section section, Semester semester, String academicSession, Boolean isActive) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.periodNumber = periodNumber;
        this.subject = subject;
        this.faculty = faculty;
        this.roomNo = roomNo;
        this.section = section;
        this.semester = semester;
        this.academicSession = academicSession;
        this.isActive = isActive != null ? isActive : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public Integer getPeriodNumber() { return periodNumber; }
    public void setPeriodNumber(Integer periodNumber) { this.periodNumber = periodNumber; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public String getAcademicSession() { return academicSession; }
    public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer periodNumber;
        private Subject subject;
        private Faculty faculty;
        private String roomNo;
        private Section section;
        private Semester semester;
        private String academicSession;
        private Boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder dayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; return this; }
        public Builder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public Builder endTime(LocalTime endTime) { this.endTime = endTime; return this; }
        public Builder periodNumber(Integer periodNumber) { this.periodNumber = periodNumber; return this; }
        public Builder subject(Subject subject) { this.subject = subject; return this; }
        public Builder faculty(Faculty faculty) { this.faculty = faculty; return this; }
        public Builder roomNo(String roomNo) { this.roomNo = roomNo; return this; }
        public Builder section(Section section) { this.section = section; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }
        public Builder academicSession(String academicSession) { this.academicSession = academicSession; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public Timetable build() {
            return new Timetable(id, dayOfWeek, startTime, endTime, periodNumber, subject, faculty, roomNo, section, semester, academicSession, isActive);
        }
    }
}
