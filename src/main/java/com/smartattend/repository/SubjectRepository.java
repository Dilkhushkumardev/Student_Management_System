package com.smartattend.repository;

import com.smartattend.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByCourseCode(String courseCode);
    List<Subject> findBySemesterId(Long semesterId);
    List<Subject> findByDepartmentId(Long departmentId);
    List<Subject> findBySemesterIdAndDepartmentId(Long semesterId, Long departmentId);
    List<Subject> findByIsActiveTrue();
    Boolean existsByCourseCode(String courseCode);

    @Query("SELECT s FROM Subject s WHERE " +
           "(:query IS NULL OR LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.courseCode) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:departmentId IS NULL OR s.department.id = :departmentId) " +
           "AND (:semesterId IS NULL OR s.semester.id = :semesterId) " +
           "AND (:isActive IS NULL OR s.isActive = :isActive)")
    Page<Subject> searchSubjects(@Param("query") String query,
                                 @Param("departmentId") Long departmentId,
                                 @Param("semesterId") Long semesterId,
                                 @Param("isActive") Boolean isActive,
                                 Pageable pageable);
}
