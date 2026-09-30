package com.smartattend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class TimetableDtoModels {

    public static class TimetableDto {
        private Long id;
        private String dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer periodNumber;
        private Long subjectId;
        private String subjectCode;
        private String subjectName;
        private Long facultyId;
        private String facultyName;
        private String roomNo;
        private Long sectionId;
        private String sectionName;
        private Long semesterId;
        private String semesterName;
        private String academicSession;
        private Boolean isActive;

        public TimetableDto() {}

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
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getSubjectCode() { return subjectCode; }
        public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public Long getFacultyId() { return facultyId; }
        public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
        public String getFacultyName() { return facultyName; }
        public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
        public String getRoomNo() { return roomNo; }
        public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public String getSectionName() { return sectionName; }
        public void setSectionName(String sectionName) { this.sectionName = sectionName; }
        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }
        public String getSemesterName() { return semesterName; }
        public void setSemesterName(String semesterName) { this.semesterName = semesterName; }
        public String getAcademicSession() { return academicSession; }
        public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final TimetableDto dto = new TimetableDto();
            public Builder id(Long id) { dto.setId(id); return this; }
            public Builder dayOfWeek(String dayOfWeek) { dto.setDayOfWeek(dayOfWeek); return this; }
            public Builder startTime(LocalTime startTime) { dto.setStartTime(startTime); return this; }
            public Builder endTime(LocalTime endTime) { dto.setEndTime(endTime); return this; }
            public Builder periodNumber(Integer periodNumber) { dto.setPeriodNumber(periodNumber); return this; }
            public Builder subjectId(Long subjectId) { dto.setSubjectId(subjectId); return this; }
            public Builder subjectCode(String subjectCode) { dto.setSubjectCode(subjectCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder facultyId(Long facultyId) { dto.setFacultyId(facultyId); return this; }
            public Builder facultyName(String facultyName) { dto.setFacultyName(facultyName); return this; }
            public Builder roomNo(String roomNo) { dto.setRoomNo(roomNo); return this; }
            public Builder sectionId(Long sectionId) { dto.setSectionId(sectionId); return this; }
            public Builder sectionName(String sectionName) { dto.setSectionName(sectionName); return this; }
            public Builder semesterId(Long semesterId) { dto.setSemesterId(semesterId); return this; }
            public Builder semesterName(String semesterName) { dto.setSemesterName(semesterName); return this; }
            public Builder academicSession(String academicSession) { dto.setAcademicSession(academicSession); return this; }
            public Builder isActive(Boolean isActive) { dto.setIsActive(isActive); return this; }
            public TimetableDto build() { return dto; }
        }
    }

    public static class TimetableCreateRequest {
        @NotBlank(message = "Day of week is required")
        private String dayOfWeek;

        @NotNull(message = "Start time is required")
        private LocalTime startTime;

        @NotNull(message = "End time is required")
        private LocalTime endTime;

        @NotNull(message = "Period number is required")
        private Integer periodNumber;

        @NotNull(message = "Subject ID is required")
        private Long subjectId;

        @NotNull(message = "Faculty ID is required")
        private Long facultyId;

        @NotBlank(message = "Room number is required")
        private String roomNo;

        @NotNull(message = "Section ID is required")
        private Long sectionId;

        @NotNull(message = "Semester ID is required")
        private Long semesterId;

        @NotBlank(message = "Academic session is required")
        private String academicSession;

        public TimetableCreateRequest() {}

        public String getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public LocalTime getStartTime() { return startTime; }
        public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
        public LocalTime getEndTime() { return endTime; }
        public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
        public Integer getPeriodNumber() { return periodNumber; }
        public void setPeriodNumber(Integer periodNumber) { this.periodNumber = periodNumber; }
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public Long getFacultyId() { return facultyId; }
        public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
        public String getRoomNo() { return roomNo; }
        public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }
        public String getAcademicSession() { return academicSession; }
        public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
    }
}
