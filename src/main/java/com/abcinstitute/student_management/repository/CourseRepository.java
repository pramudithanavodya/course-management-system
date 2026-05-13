<<<<<<< HEAD
package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByDepartmentId(Long departmentId);
=======
package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByDepartmentId(Long departmentId);
>>>>>>> 0331f75549f6d4f92c280c11ecd72798cdeffd65
}