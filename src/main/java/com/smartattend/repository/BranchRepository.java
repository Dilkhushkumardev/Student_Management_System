package com.smartattend.repository;

import com.smartattend.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByCode(String code);
    List<Branch> findByDepartmentId(Long departmentId);
    List<Branch> findByIsActiveTrue();
}
