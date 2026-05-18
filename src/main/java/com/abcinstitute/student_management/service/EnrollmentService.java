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

    @Autowired private EnrollmentRepository    enrollmentRepository;
    @Autowired private CourseRepository        courseRepository;
    @Autowired private EnrollmentLogRepository enrollmentLogRepository;

    // ── FIX 1: inject NotificationService ────────────────────────
    @Autowired private NotificationService notificationService;

    // ── CREATE: Enroll student after validating key ───────────────
    public String enrollStudent(String username, Long courseId, String key) {

        if (enrollmentRepository.existsByStudentUsernameAndCourseId(username, courseId)) {
            return "ALREADY_ENROLLED";
        }

        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) return "COURSE_NOT_FOUND";

        Course course = courseOpt.get();
        if (!course.getEnrollmentKey().equals(key)) return "WRONG_KEY";

        // Save enrollment
        enrollmentRepository.save(new Enrollment(username, course));

        // Save audit log
        enrollmentLogRepository.save(new EnrollmentLog(
                username, course.getCourseCode(), course.getCourseName()));

        // ── FIX 2: notify student they enrolled ──────────────────
        notificationService.notifyEnrolled(username, course.getCourseName());

        return "SUCCESS";
    }

    // ── READ: Get enrolled courses for a student ──────────────────
    public List<Course> getEnrolledCourses(String username) {
        return enrollmentRepository.findByStudentUsername(username)
                .stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    // ── READ: Check if already enrolled ──────────────────────────
    public boolean isEnrolled(String username, Long courseId) {
        return enrollmentRepository
                .existsByStudentUsernameAndCourseId(username, courseId);
    }

    // ── DELETE: Unenroll / drop a course ──────────────────────────
    @Transactional
    public boolean unenrollStudent(String username, Long courseId) {
        if (!enrollmentRepository.existsByStudentUsernameAndCourseId(username, courseId)) {
            return false;
        }

        // ── FIX 3: look up course name BEFORE deleting so we can notify ──
        String courseName = courseRepository.findById(courseId)
                .map(Course::getCourseName)
                .orElse("Unknown Course");

        enrollmentRepository.deleteByStudentUsernameAndCourseId(username, courseId);

        // ── FIX 4: notify student they unenrolled ────────────────
        notificationService.notifyUnenrolled(username, courseName);

        return true;
    }
}
