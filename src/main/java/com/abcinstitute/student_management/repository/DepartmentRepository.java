
package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * OOP CONCEPT: INHERITANCE & ABSTRACTION
 * - Inheritance (Interface Inheritance): DepartmentRepository extends JpaRepository, meaning it inherits
 *   all database operation contracts (CRUD methods like save, findAll, findById, delete, etc.) from the parent JpaRepository interface.
 * - Abstraction: As a Java interface, it defines 'what' database operations can be performed without specifying
 *   'how' they are implemented. Spring Data JPA automatically provides the runtime implementation details.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // OOP CONCEPT: ABSTRACTION
    // Method signature defines the contract for checking a department's existence by its code.
    boolean existsByDeptCode(String deptCode);

}