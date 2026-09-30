package com.smartattend.repository;

import com.smartattend.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByDepartmentIdAndSemesterId(Long departmentId, Long semesterId);
    List<Section> findBySemesterId(Long semesterId);
    List<Section> findByIsActiveTrue();
}
