package com.smartattend.repository;

import com.smartattend.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByUniversityRegNo(String universityRegNo);
    Optional<Student> findByRollNo(String rollNo);
    Optional<Student> findByBiometricId(String biometricId);
    Optional<Student> findByEmail(String email);

    List<Student> findBySectionId(Long sectionId);
    List<Student> findBySemesterId(Long semesterId);
    List<Student> findByDepartmentId(Long departmentId);
    List<Student> findBySectionIdOrderByRollNoAsc(Long sectionId);

    Boolean existsByUniversityRegNo(String universityRegNo);
    Boolean existsByRollNo(String rollNo);
    Boolean existsByEmail(String email);
    Boolean existsByBiometricId(String biometricId);

    @Query("SELECT s FROM Student s WHERE " +
           "(:query IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.rollNo) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.universityRegNo) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.biometricId) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:departmentId IS NULL OR s.department.id = :departmentId) " +
           "AND (:semesterId IS NULL OR s.semester.id = :semesterId) " +
           "AND (:sectionId IS NULL OR s.section.id = :sectionId) " +
           "AND (:status IS NULL OR s.status = :status)")
    Page<Student> searchStudents(@Param("query") String query,
                                 @Param("departmentId") Long departmentId,
                                 @Param("semesterId") Long semesterId,
                                 @Param("sectionId") Long sectionId,
                                 @Param("status") String status,
                                 Pageable pageable);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.status = 'ACTIVE'")
    long countActiveStudents();
}
