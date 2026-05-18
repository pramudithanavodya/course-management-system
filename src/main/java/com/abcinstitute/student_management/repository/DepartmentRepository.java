
package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// OOP Concept: Inheritance - inherits database methods from JpaRepository
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // OOP Concept: Abstraction - interface method signature hiding query logic
    boolean existsByDeptCode(String deptCode);

}