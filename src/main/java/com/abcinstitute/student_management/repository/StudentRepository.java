<<<<<<< HEAD
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
=======
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
>>>>>>> 0331f75549f6d4f92c280c11ecd72798cdeffd65
}