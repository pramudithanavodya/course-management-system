package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentUsername(String username);
    Optional<Enrollment> findByStudentUsernameAndCourseId(String username, Long courseId);
    boolean existsByStudentUsernameAndCourseId(String username, Long courseId);
}