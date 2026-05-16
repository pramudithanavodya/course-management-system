package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.CourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long> {
}
