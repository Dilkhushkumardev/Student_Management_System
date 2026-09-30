package com.smartattend.repository;

import com.smartattend.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findBySubjectId(Long subjectId);
    List<Enrollment> findBySectionId(Long sectionId);
    List<Enrollment> findByStudentIdAndSemesterId(Long studentId, Long semesterId);
    Optional<Enrollment> findByStudentIdAndSubjectIdAndSemesterId(Long studentId, Long subjectId, Long semesterId);

    @Query("SELECT e.student.id FROM Enrollment e WHERE e.subject.id = :subjectId AND e.section.id = :sectionId AND e.enrollmentStatus = 'ACTIVE'")
    List<Long> findStudentIdsBySubjectAndSection(@Param("subjectId") Long subjectId, @Param("sectionId") Long sectionId);

    @Query("SELECT e FROM Enrollment e WHERE e.subject.id = :subjectId AND e.section.id = :sectionId AND e.enrollmentStatus = 'ACTIVE' ORDER BY e.student.rollNo ASC")
    List<Enrollment> findActiveEnrollmentsBySubjectAndSection(@Param("subjectId") Long subjectId, @Param("sectionId") Long sectionId);

    Boolean existsByStudentIdAndSubjectIdAndSemesterId(Long studentId, Long subjectId, Long semesterId);
}
