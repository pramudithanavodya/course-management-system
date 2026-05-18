
package com.abcinstitute.student_management.service;

import com.abcinstitute.student_management.model.Course;
import com.abcinstitute.student_management.model.Enrollment;
import com.abcinstitute.student_management.model.EnrollmentLog;
import com.abcinstitute.student_management.repository.CourseRepository;
import com.abcinstitute.student_management.repository.EnrollmentLogRepository;
import com.abcinstitute.student_management.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentLogRepository enrollmentLogRepository;

    // Enroll student after validating key
    public String enrollStudent(String username, Long courseId, String key) {

        if (enrollmentRepository
                .existsByStudentUsernameAndCourseId(username, courseId)) {
            return "ALREADY_ENROLLED";
        }

        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) return "COURSE_NOT_FOUND";

        Course course = courseOpt.get();
        if (!course.getEnrollmentKey().equals(key)) return "WRONG_KEY";

        // Save enrollment to MySQL
        Enrollment enrollment = new Enrollment(username, course);
        enrollmentRepository.save(enrollment);

        // Save log to enrollment_logs table (replaces enrollments_backup.txt)
        EnrollmentLog log = new EnrollmentLog(
                username,
                course.getCourseCode(),
                course.getCourseName()
        );
        enrollmentLogRepository.save(log);

        return "SUCCESS";
    }

    public List<Course> getEnrolledCourses(String username) {
        return enrollmentRepository.findByStudentUsername(username)
                .stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    public boolean isEnrolled(String username, Long courseId) {
        return enrollmentRepository
                .existsByStudentUsernameAndCourseId(username, courseId);
    }
    // ✅ ADD THIS METHOD at the bottom of EnrollmentService class
    @Transactional
    public boolean unenrollStudent(String username, Long courseId) {
        if (!enrollmentRepository.existsByStudentUsernameAndCourseId(username, courseId)) {
            return false; // not enrolled, nothing to delete
        }
        enrollmentRepository.deleteByStudentUsernameAndCourseId(username, courseId);
        return true;
    }
}
