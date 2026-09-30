package com.smartattend.repository;

import com.smartattend.entity.FacultySubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultySubjectRepository extends JpaRepository<FacultySubject, Long> {
    List<FacultySubject> findByFacultyId(Long facultyId);
    List<FacultySubject> findByFacultyIdAndIsActiveTrue(Long facultyId);
    List<FacultySubject> findBySubjectId(Long subjectId);
    List<FacultySubject> findBySectionId(Long sectionId);
    Optional<FacultySubject> findByFacultyIdAndSubjectIdAndSectionId(Long facultyId, Long subjectId, Long sectionId);
    Boolean existsByFacultyIdAndSubjectIdAndSectionId(Long facultyId, Long subjectId, Long sectionId);
}
