
package com.abcinstitute.student_management.service;

import com.abcinstitute.student_management.model.Course;
import com.abcinstitute.student_management.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // CREATE
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    // READ ALL
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // READ ONE
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // UPDATE (same as save — JPA handles it)
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    // DELETE
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    // Validate enrollment key
    public boolean validateEnrollmentKey(Long courseId, String key) {
        return courseRepository.findById(courseId)
                .map(c -> c.getEnrollmentKey().equals(key))
                .orElse(false);
    }
}