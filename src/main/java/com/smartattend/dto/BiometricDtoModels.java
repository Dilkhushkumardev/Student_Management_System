package com.smartattend.dto;

import com.smartattend.enums.BiometricResult;
import com.smartattend.enums.DeviceStatus;
import com.smartattend.enums.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BiometricDtoModels {

    public static class BiometricDeviceDto {
        private Long id;
        private String deviceCode;
        private String deviceName;
        private String serialNumber;
        private String ipAddress;
        private Integer port;
        private String location;
        private DeviceType deviceType;
        private DeviceStatus status;
        private LocalDateTime lastHeartbeat;
        private LocalDateTime lastSyncTime;
        private Boolean isActive;

        public BiometricDeviceDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDeviceCode() { return deviceCode; }
        public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
        public String getDeviceName() { return deviceName; }
        public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public Integer getPort() { return port; }
        public void setPort(Integer port) { this.port = port; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public DeviceType getDeviceType() { return deviceType; }
        public void setDeviceType(DeviceType deviceType) { this.deviceType = deviceType; }
        public DeviceStatus getStatus() { return status; }
        public void setStatus(DeviceStatus status) { this.status = status; }
        public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
        public LocalDateTime getLastSyncTime() { return lastSyncTime; }
        public void setLastSyncTime(LocalDateTime lastSyncTime) { this.lastSyncTime = lastSyncTime; }
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final BiometricDeviceDto dto = new BiometricDeviceDto();
            public Builder id(Long id) { dto.setId(id); return this; }
            public Builder deviceCode(String deviceCode) { dto.setDeviceCode(deviceCode); return this; }
            public Builder deviceName(String deviceName) { dto.setDeviceName(deviceName); return this; }
            public Builder serialNumber(String serialNumber) { dto.setSerialNumber(serialNumber); return this; }
            public Builder ipAddress(String ipAddress) { dto.setIpAddress(ipAddress); return this; }
            public Builder port(Integer port) { dto.setPort(port); return this; }
            public Builder location(String location) { dto.setLocation(location); return this; }
            public Builder deviceType(DeviceType deviceType) { dto.setDeviceType(deviceType); return this; }
            public Builder status(DeviceStatus status) { dto.setStatus(status); return this; }
            public Builder lastHeartbeat(LocalDateTime lastHeartbeat) { dto.setLastHeartbeat(lastHeartbeat); return this; }
            public Builder lastSyncTime(LocalDateTime lastSyncTime) { dto.setLastSyncTime(lastSyncTime); return this; }
            public Builder isActive(Boolean isActive) { dto.setIsActive(isActive); return this; }
            public BiometricDeviceDto build() { return dto; }
        }
    }

    public static class BiometricDeviceCreateRequest {
        @NotBlank(message = "Device code is required")
        private String deviceCode;

        @NotBlank(message = "Device name is required")
        private String deviceName;

        @NotBlank(message = "Serial number is required")
        private String serialNumber;

        private String ipAddress;
        private Integer port;
        private String location;
        private DeviceType deviceType;
        private String apiKey;

        public BiometricDeviceCreateRequest() {}

        public String getDeviceCode() { return deviceCode; }
        public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
        public String getDeviceName() { return deviceName; }
        public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public Integer getPort() { return port; }
        public void setPort(Integer port) { this.port = port; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public DeviceType getDeviceType() { return deviceType; }
        public void setDeviceType(DeviceType deviceType) { this.deviceType = deviceType; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    }

    public static class BiometricEventRequest {
        @NotBlank(message = "Device serial or code is required")
        private String deviceIdentifier;

        @NotBlank(message = "Biometric user ID / Student Roll / Biometric ID is required")
        private String biometricUserId;

        private LocalDateTime eventTimestamp;
        private String verificationType;
        private String deviceLocation;
        private Long targetSessionId;
        private String rawPayload;

        public BiometricEventRequest() {}

        public String getDeviceIdentifier() { return deviceIdentifier; }
        public void setDeviceIdentifier(String deviceIdentifier) { this.deviceIdentifier = deviceIdentifier; }
        public String getBiometricUserId() { return biometricUserId; }
        public void setBiometricUserId(String biometricUserId) { this.biometricUserId = biometricUserId; }
        public LocalDateTime getEventTimestamp() { return eventTimestamp; }
        public void setEventTimestamp(LocalDateTime eventTimestamp) { this.eventTimestamp = eventTimestamp; }
        public String getVerificationType() { return verificationType; }
        public void setVerificationType(String verificationType) { this.verificationType = verificationType; }
        public String getDeviceLocation() { return deviceLocation; }
        public void setDeviceLocation(String deviceLocation) { this.deviceLocation = deviceLocation; }
        public Long getTargetSessionId() { return targetSessionId; }
        public void setTargetSessionId(Long targetSessionId) { this.targetSessionId = targetSessionId; }
        public String getRawPayload() { return rawPayload; }
        public void setRawPayload(String rawPayload) { this.rawPayload = rawPayload; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final BiometricEventRequest req = new BiometricEventRequest();
            public Builder deviceIdentifier(String deviceIdentifier) { req.setDeviceIdentifier(deviceIdentifier); return this; }
            public Builder biometricUserId(String biometricUserId) { req.setBiometricUserId(biometricUserId); return this; }
            public Builder eventTimestamp(LocalDateTime eventTimestamp) { req.setEventTimestamp(eventTimestamp); return this; }
            public Builder verificationType(String verificationType) { req.setVerificationType(verificationType); return this; }
            public Builder deviceLocation(String deviceLocation) { req.setDeviceLocation(deviceLocation); return this; }
            public Builder targetSessionId(Long targetSessionId) { req.setTargetSessionId(targetSessionId); return this; }
            public Builder rawPayload(String rawPayload) { req.setRawPayload(rawPayload); return this; }
            public BiometricEventRequest build() { return req; }
        }
    }

    public static class BiometricEventResponse {
        private Boolean success;
        private BiometricResult result;
        private String message;
        private Long eventId;
        private Long studentId;
        private String studentName;
        private String rollNo;
        private Long sessionId;
        private String subjectName;
        private LocalDateTime timestamp;
        private BiometricLiveCounterDto updatedCounter;

        public BiometricEventResponse() {}

        public Boolean getSuccess() { return success; }
        public void setSuccess(Boolean success) { this.success = success; }
        public BiometricResult getResult() { return result; }
        public void setResult(BiometricResult result) { this.result = result; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getRollNo() { return rollNo; }
        public void setRollNo(String rollNo) { this.rollNo = rollNo; }
        public Long getSessionId() { return sessionId; }
        public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public BiometricLiveCounterDto getUpdatedCounter() { return updatedCounter; }
        public void setUpdatedCounter(BiometricLiveCounterDto updatedCounter) { this.updatedCounter = updatedCounter; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final BiometricEventResponse res = new BiometricEventResponse();
            public Builder success(Boolean success) { res.setSuccess(success); return this; }
            public Builder result(BiometricResult result) { res.setResult(result); return this; }
            public Builder message(String message) { res.setMessage(message); return this; }
            public Builder eventId(Long eventId) { res.setEventId(eventId); return this; }
            public Builder studentId(Long studentId) { res.setStudentId(studentId); return this; }
            public Builder studentName(String studentName) { res.setStudentName(studentName); return this; }
            public Builder rollNo(String rollNo) { res.setRollNo(rollNo); return this; }
            public Builder sessionId(Long sessionId) { res.setSessionId(sessionId); return this; }
            public Builder subjectName(String subjectName) { res.setSubjectName(subjectName); return this; }
            public Builder timestamp(LocalDateTime timestamp) { res.setTimestamp(timestamp); return this; }
            public Builder updatedCounter(BiometricLiveCounterDto updatedCounter) { res.setUpdatedCounter(updatedCounter); return this; }
            public BiometricEventResponse build() { return res; }
        }
    }

    public static class BiometricLiveCounterDto {
        private Long sessionId;
        private String sessionCode;
        private String subjectName;
        private String sectionName;
        private String facultyName;
        private Integer totalStudents;
        private Integer presentCount;
        private Integer absentCount;
        private Integer lateCount;
        private Integer excusedCount;
        private Integer notVerifiedCount;
        private BigDecimal attendancePercentage;
        private Boolean isLocked;
        private List<LiveStudentFeedDto> recentFeed;
        private LocalDateTime lastUpdatedAt;

        public BiometricLiveCounterDto() {}

        public Long getSessionId() { return sessionId; }
        public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
        public String getSessionCode() { return sessionCode; }
        public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public String getSectionName() { return sectionName; }
        public void setSectionName(String sectionName) { this.sectionName = sectionName; }
        public String getFacultyName() { return facultyName; }
        public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
        public Integer getTotalStudents() { return totalStudents; }
        public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
        public Integer getPresentCount() { return presentCount; }
        public void setPresentCount(Integer presentCount) { this.presentCount = presentCount; }
        public Integer getAbsentCount() { return absentCount; }
        public void setAbsentCount(Integer absentCount) { this.absentCount = absentCount; }
        public Integer getLateCount() { return lateCount; }
        public void setLateCount(Integer lateCount) { this.lateCount = lateCount; }
        public Integer getExcusedCount() { return excusedCount; }
        public void setExcusedCount(Integer excusedCount) { this.excusedCount = excusedCount; }
        public Integer getNotVerifiedCount() { return notVerifiedCount; }
        public void setNotVerifiedCount(Integer notVerifiedCount) { this.notVerifiedCount = notVerifiedCount; }
        public BigDecimal getAttendancePercentage() { return attendancePercentage; }
        public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }
        public Boolean getIsLocked() { return isLocked; }
        public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }
        public List<LiveStudentFeedDto> getRecentFeed() { return recentFeed; }
        public void setRecentFeed(List<LiveStudentFeedDto> recentFeed) { this.recentFeed = recentFeed; }
        public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
        public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final BiometricLiveCounterDto dto = new BiometricLiveCounterDto();
            public Builder sessionId(Long sessionId) { dto.setSessionId(sessionId); return this; }
            public Builder sessionCode(String sessionCode) { dto.setSessionCode(sessionCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder sectionName(String sectionName) { dto.setSectionName(sectionName); return this; }
            public Builder facultyName(String facultyName) { dto.setFacultyName(facultyName); return this; }
            public Builder totalStudents(Integer totalStudents) { dto.setTotalStudents(totalStudents); return this; }
            public Builder presentCount(Integer presentCount) { dto.setPresentCount(presentCount); return this; }
            public Builder absentCount(Integer absentCount) { dto.setAbsentCount(absentCount); return this; }
            public Builder lateCount(Integer lateCount) { dto.setLateCount(lateCount); return this; }
            public Builder excusedCount(Integer excusedCount) { dto.setExcusedCount(excusedCount); return this; }
            public Builder notVerifiedCount(Integer notVerifiedCount) { dto.setNotVerifiedCount(notVerifiedCount); return this; }
            public Builder attendancePercentage(BigDecimal attendancePercentage) { dto.setAttendancePercentage(attendancePercentage); return this; }
            public Builder isLocked(Boolean isLocked) { dto.setIsLocked(isLocked); return this; }
            public Builder recentFeed(List<LiveStudentFeedDto> recentFeed) { dto.setRecentFeed(recentFeed); return this; }
            public Builder lastUpdatedAt(LocalDateTime lastUpdatedAt) { dto.setLastUpdatedAt(lastUpdatedAt); return this; }
            public BiometricLiveCounterDto build() { return dto; }
        }
    }

    public static class LiveStudentFeedDto {
        private Long recordId;
        private Long studentId;
        private String rollNo;
        private String studentName;
        private String verificationType;
        private String verificationResult;
        private String timeAgo;
        private LocalDateTime timestamp;

        public LiveStudentFeedDto() {}

        public Long getRecordId() { return recordId; }
        public void setRecordId(Long recordId) { this.recordId = recordId; }
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getRollNo() { return rollNo; }
        public void setRollNo(String rollNo) { this.rollNo = rollNo; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getVerificationType() { return verificationType; }
        public void setVerificationType(String verificationType) { this.verificationType = verificationType; }
        public String getVerificationResult() { return verificationResult; }
        public void setVerificationResult(String verificationResult) { this.verificationResult = verificationResult; }
        public String getTimeAgo() { return timeAgo; }
        public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final LiveStudentFeedDto dto = new LiveStudentFeedDto();
            public Builder recordId(Long recordId) { dto.setRecordId(recordId); return this; }
            public Builder studentId(Long studentId) { dto.setStudentId(studentId); return this; }
            public Builder rollNo(String rollNo) { dto.setRollNo(rollNo); return this; }
            public Builder studentName(String studentName) { dto.setStudentName(studentName); return this; }
            public Builder verificationType(String verificationType) { dto.setVerificationType(verificationType); return this; }
            public Builder verificationResult(String verificationResult) { dto.setVerificationResult(verificationResult); return this; }
            public Builder timeAgo(String timeAgo) { dto.setTimeAgo(timeAgo); return this; }
            public Builder timestamp(LocalDateTime timestamp) { dto.setTimestamp(timestamp); return this; }
            public LiveStudentFeedDto build() { return dto; }
        }
    }

    public static class BiometricSimulationRequest {
        @NotNull(message = "Session ID is required")
        private Long sessionId;
        private Long studentId;
        private String verificationType;
        private Boolean simulateAllRemaining;

        public BiometricSimulationRequest() {}

        public Long getSessionId() { return sessionId; }
        public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getVerificationType() { return verificationType; }
        public void setVerificationType(String verificationType) { this.verificationType = verificationType; }
        public Boolean getSimulateAllRemaining() { return simulateAllRemaining; }
        public void setSimulateAllRemaining(Boolean simulateAllRemaining) { this.simulateAllRemaining = simulateAllRemaining; }
    }
}
