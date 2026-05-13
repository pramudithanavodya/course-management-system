package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.EnrollmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentLogRepository extends JpaRepository<EnrollmentLog, Long> {
    List<EnrollmentLog> findByStudentUsername(String username);
}