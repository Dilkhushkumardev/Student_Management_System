package com.smartattend.entity;

import com.smartattend.enums.DeviceStatus;
import com.smartattend.enums.DeviceType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "biometric_devices")
public class BiometricDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_code", nullable = false, unique = true, length = 50)
    private String deviceCode;

    @Column(name = "device_name", nullable = false, length = 150)
    private String deviceName;

    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column
    private Integer port = 80;

    @Column(length = 150)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false, length = 30)
    private DeviceType deviceType = DeviceType.FINGERPRINT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeviceStatus status = DeviceStatus.ONLINE;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "last_sync_time")
    private LocalDateTime lastSyncTime;

    @Column(name = "api_key", unique = true, length = 100)
    private String apiKey;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public BiometricDevice() {}

    public BiometricDevice(Long id, String deviceCode, String deviceName, String serialNumber, String ipAddress, Integer port, String location, DeviceType deviceType, DeviceStatus status, LocalDateTime lastHeartbeat, LocalDateTime lastSyncTime, String apiKey, Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.deviceCode = deviceCode;
        this.deviceName = deviceName;
        this.serialNumber = serialNumber;
        this.ipAddress = ipAddress;
        this.port = port != null ? port : 80;
        this.location = location;
        this.deviceType = deviceType != null ? deviceType : DeviceType.FINGERPRINT;
        this.status = status != null ? status : DeviceStatus.ONLINE;
        this.lastHeartbeat = lastHeartbeat;
        this.lastSyncTime = lastSyncTime;
        this.apiKey = apiKey;
        this.isActive = isActive != null ? isActive : true;
        this.createdAt = createdAt;
    }

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
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String deviceCode;
        private String deviceName;
        private String serialNumber;
        private String ipAddress;
        private Integer port = 80;
        private String location;
        private DeviceType deviceType = DeviceType.FINGERPRINT;
        private DeviceStatus status = DeviceStatus.ONLINE;
        private LocalDateTime lastHeartbeat;
        private LocalDateTime lastSyncTime;
        private String apiKey;
        private Boolean isActive = true;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder deviceCode(String deviceCode) { this.deviceCode = deviceCode; return this; }
        public Builder deviceName(String deviceName) { this.deviceName = deviceName; return this; }
        public Builder serialNumber(String serialNumber) { this.serialNumber = serialNumber; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder port(Integer port) { this.port = port; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder deviceType(DeviceType deviceType) { this.deviceType = deviceType; return this; }
        public Builder status(DeviceStatus status) { this.status = status; return this; }
        public Builder lastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; return this; }
        public Builder lastSyncTime(LocalDateTime lastSyncTime) { this.lastSyncTime = lastSyncTime; return this; }
        public Builder apiKey(String apiKey) { this.apiKey = apiKey; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public BiometricDevice build() {
            return new BiometricDevice(id, deviceCode, deviceName, serialNumber, ipAddress, port, location, deviceType, status, lastHeartbeat, lastSyncTime, apiKey, isActive, createdAt);
        }
    }
}
