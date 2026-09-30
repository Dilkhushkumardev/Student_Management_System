package com.smartattend.repository;

import com.smartattend.entity.BiometricUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BiometricUserRepository extends JpaRepository<BiometricUser, Long> {
    Optional<BiometricUser> findByBiometricId(String biometricId);
    Optional<BiometricUser> findByDeviceUserId(String deviceUserId);
    Optional<BiometricUser> findByStudentId(Long studentId);
    Optional<BiometricUser> findByFacultyId(Long facultyId);
}
