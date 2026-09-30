package com.smartattend.repository;

import com.smartattend.entity.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByUserId(Long userId);
    Optional<Faculty> findByEmployeeId(String employeeId);
    Optional<Faculty> findByEmail(String email);
    List<Faculty> findByDepartmentId(Long departmentId);
    List<Faculty> findByStatus(String status);
    Boolean existsByEmployeeId(String employeeId);
    Boolean existsByEmail(String email);

    @Query("SELECT f FROM Faculty f WHERE " +
           "(:query IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(f.employeeId) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(f.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(f.designation) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:departmentId IS NULL OR f.department.id = :departmentId) " +
           "AND (:status IS NULL OR f.status = :status)")
    Page<Faculty> searchFaculty(@Param("query") String query,
                                @Param("departmentId") Long departmentId,
                                @Param("status") String status,
                                Pageable pageable);
}
