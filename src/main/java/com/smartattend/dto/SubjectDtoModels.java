package com.smartattend.dto;

import com.smartattend.enums.SubjectType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SubjectDtoModels {

    public static class SubjectDto {
        private Long id;
        private String courseCode;
        private String subjectName;
        private SubjectType subjectType;
        private Integer lectureHours;
        private Integer tutorialHours;
        private Integer practicalHours;
        private BigDecimal credits;
        private Integer maxInternalMarks;
        private Integer maxEseMarks;
        private Long semesterId;
        private String semesterName;
        private Long departmentId;
        private String departmentName;
        private String academicSession;
        private Boolean attendanceRequired;
        private Boolean isActive;

        public SubjectDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public SubjectType getSubjectType() { return subjectType; }
        public void setSubjectType(SubjectType subjectType) { this.subjectType = subjectType; }
        public Integer getLectureHours() { return lectureHours; }
        public void setLectureHours(Integer lectureHours) { this.lectureHours = lectureHours; }
        public Integer getTutorialHours() { return tutorialHours; }
        public void setTutorialHours(Integer tutorialHours) { this.tutorialHours = tutorialHours; }
        public Integer getPracticalHours() { return practicalHours; }
        public void setPracticalHours(Integer practicalHours) { this.practicalHours = practicalHours; }
        public BigDecimal getCredits() { return credits; }
        public void setCredits(BigDecimal credits) { this.credits = credits; }
        public Integer getMaxInternalMarks() { return maxInternalMarks; }
        public void setMaxInternalMarks(Integer maxInternalMarks) { this.maxInternalMarks = maxInternalMarks; }
        public Integer getMaxEseMarks() { return maxEseMarks; }
        public void setMaxEseMarks(Integer maxEseMarks) { this.maxEseMarks = maxEseMarks; }
        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }
        public String getSemesterName() { return semesterName; }
        public void setSemesterName(String semesterName) { this.semesterName = semesterName; }
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public String getAcademicSession() { return academicSession; }
        public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
        public Boolean getAttendanceRequired() { return attendanceRequired; }
        public void setAttendanceRequired(Boolean attendanceRequired) { this.attendanceRequired = attendanceRequired; }
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final SubjectDto dto = new SubjectDto();
            public Builder id(Long id) { dto.setId(id); return this; }
            public Builder courseCode(String courseCode) { dto.setCourseCode(courseCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder subjectType(SubjectType subjectType) { dto.setSubjectType(subjectType); return this; }
            public Builder lectureHours(Integer lectureHours) { dto.setLectureHours(lectureHours); return this; }
            public Builder tutorialHours(Integer tutorialHours) { dto.setTutorialHours(tutorialHours); return this; }
            public Builder practicalHours(Integer practicalHours) { dto.setPracticalHours(practicalHours); return this; }
            public Builder credits(BigDecimal credits) { dto.setCredits(credits); return this; }
            public Builder maxInternalMarks(Integer maxInternalMarks) { dto.setMaxInternalMarks(maxInternalMarks); return this; }
            public Builder maxEseMarks(Integer maxEseMarks) { dto.setMaxEseMarks(maxEseMarks); return this; }
            public Builder semesterId(Long semesterId) { dto.setSemesterId(semesterId); return this; }
            public Builder semesterName(String semesterName) { dto.setSemesterName(semesterName); return this; }
            public Builder departmentId(Long departmentId) { dto.setDepartmentId(departmentId); return this; }
            public Builder departmentName(String departmentName) { dto.setDepartmentName(departmentName); return this; }
            public Builder academicSession(String academicSession) { dto.setAcademicSession(academicSession); return this; }
            public Builder attendanceRequired(Boolean attendanceRequired) { dto.setAttendanceRequired(attendanceRequired); return this; }
            public Builder isActive(Boolean isActive) { dto.setIsActive(isActive); return this; }
            public SubjectDto build() { return dto; }
        }
    }

    public static class SubjectCreateRequest {
        @NotBlank(message = "Course code is required")
        private String courseCode;

        @NotBlank(message = "Subject name is required")
        private String subjectName;

        @NotNull(message = "Subject type is required")
        private SubjectType subjectType;

        private Integer lectureHours;
        private Integer tutorialHours;
        private Integer practicalHours;

        @NotNull(message = "Credits are required")
        private BigDecimal credits;

        private Integer maxInternalMarks;
        private Integer maxEseMarks;

        @NotNull(message = "Semester ID is required")
        private Long semesterId;

        @NotNull(message = "Department ID is required")
        private Long departmentId;

        @NotBlank(message = "Academic session is required")
        private String academicSession;

        private Boolean attendanceRequired;

        public SubjectCreateRequest() {}

        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public SubjectType getSubjectType() { return subjectType; }
        public void setSubjectType(SubjectType subjectType) { this.subjectType = subjectType; }
        public Integer getLectureHours() { return lectureHours; }
        public void setLectureHours(Integer lectureHours) { this.lectureHours = lectureHours; }
        public Integer getTutorialHours() { return tutorialHours; }
        public void setTutorialHours(Integer tutorialHours) { this.tutorialHours = tutorialHours; }
        public Integer getPracticalHours() { return practicalHours; }
        public void setPracticalHours(Integer practicalHours) { this.practicalHours = practicalHours; }
        public BigDecimal getCredits() { return credits; }
        public void setCredits(BigDecimal credits) { this.credits = credits; }
        public Integer getMaxInternalMarks() { return maxInternalMarks; }
        public void setMaxInternalMarks(Integer maxInternalMarks) { this.maxInternalMarks = maxInternalMarks; }
        public Integer getMaxEseMarks() { return maxEseMarks; }
        public void setMaxEseMarks(Integer maxEseMarks) { this.maxEseMarks = maxEseMarks; }
        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
        public String getAcademicSession() { return academicSession; }
        public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
        public Boolean getAttendanceRequired() { return attendanceRequired; }
        public void setAttendanceRequired(Boolean attendanceRequired) { this.attendanceRequired = attendanceRequired; }
    }

    public static class SubjectUpdateRequest {
        @NotBlank(message = "Subject name is required")
        private String subjectName;
        private SubjectType subjectType;
        private Integer lectureHours;
        private Integer tutorialHours;
        private Integer practicalHours;
        private BigDecimal credits;
        private Integer maxInternalMarks;
        private Integer maxEseMarks;
        private Boolean attendanceRequired;
        private Boolean isActive;

        public SubjectUpdateRequest() {}

        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public SubjectType getSubjectType() { return subjectType; }
        public void setSubjectType(SubjectType subjectType) { this.subjectType = subjectType; }
        public Integer getLectureHours() { return lectureHours; }
        public void setLectureHours(Integer lectureHours) { this.lectureHours = lectureHours; }
        public Integer getTutorialHours() { return tutorialHours; }
        public void setTutorialHours(Integer tutorialHours) { this.tutorialHours = tutorialHours; }
        public Integer getPracticalHours() { return practicalHours; }
        public void setPracticalHours(Integer practicalHours) { this.practicalHours = practicalHours; }
        public BigDecimal getCredits() { return credits; }
        public void setCredits(BigDecimal credits) { this.credits = credits; }
        public Integer getMaxInternalMarks() { return maxInternalMarks; }
        public void setMaxInternalMarks(Integer maxInternalMarks) { this.maxInternalMarks = maxInternalMarks; }
        public Integer getMaxEseMarks() { return maxEseMarks; }
        public void setMaxEseMarks(Integer maxEseMarks) { this.maxEseMarks = maxEseMarks; }
        public Boolean getAttendanceRequired() { return attendanceRequired; }
        public void setAttendanceRequired(Boolean attendanceRequired) { this.attendanceRequired = attendanceRequired; }
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    }
}
