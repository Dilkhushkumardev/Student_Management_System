package com.smartattend.repository;

import com.smartattend.entity.BiometricDevice;
import com.smartattend.enums.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BiometricDeviceRepository extends JpaRepository<BiometricDevice, Long> {
    Optional<BiometricDevice> findByDeviceCode(String deviceCode);
    Optional<BiometricDevice> findBySerialNumber(String serialNumber);
    Optional<BiometricDevice> findByApiKey(String apiKey);
    List<BiometricDevice> findByStatus(DeviceStatus status);
    List<BiometricDevice> findByIsActiveTrue();
    long countByStatus(DeviceStatus status);
}
