
package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "course_content", columnDefinition = "TEXT")
    private String courseContent;

    @Column(name = "lecture_hours_per_week")
    private Integer lectureHoursPerWeek;

    @Column(name = "course_fee")
    private BigDecimal courseFee;

    @Column(name = "enrollment_key", nullable = false)
    private String enrollmentKey;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecture lecturer;

    public Course() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseContent() { return courseContent; }
    public void setCourseContent(String courseContent) { this.courseContent = courseContent; }

    public Integer getLectureHoursPerWeek() { return lectureHoursPerWeek; }
    public void setLectureHoursPerWeek(Integer lectureHoursPerWeek) { this.lectureHoursPerWeek = lectureHoursPerWeek; }

    public BigDecimal getCourseFee() { return courseFee; }
    public void setCourseFee(BigDecimal courseFee) { this.courseFee = courseFee; }

    public String getEnrollmentKey() { return enrollmentKey; }
    public void setEnrollmentKey(String enrollmentKey) { this.enrollmentKey = enrollmentKey; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Lecture getLecturer() { return lecturer; }
    public void setLecturer(Lecture lecturer) { this.lecturer = lecturer; }
}