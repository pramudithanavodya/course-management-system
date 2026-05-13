package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDeptCode(String deptCode);
}