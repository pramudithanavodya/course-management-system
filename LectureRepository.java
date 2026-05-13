package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByDepartmentId(Long departmentId);
}