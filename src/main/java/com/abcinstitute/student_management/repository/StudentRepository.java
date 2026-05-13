package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}