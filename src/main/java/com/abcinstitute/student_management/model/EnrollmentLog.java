package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_logs")
public class EnrollmentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_username", nullable = false)
    private String studentUsername;

    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "enrolled_date")
    private LocalDateTime enrolledDate;

    public EnrollmentLog() {}

    public EnrollmentLog(String studentUsername, String courseCode,
                         String courseName) {
        this.studentUsername = studentUsername;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.enrolledDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String u) { this.studentUsername = u; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String c) { this.courseCode = c; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String n) { this.courseName = n; }

    public LocalDateTime getEnrolledDate() { return enrolledDate; }
    public void setEnrolledDate(LocalDateTime d) { this.enrolledDate = d; }
}