package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import java.util.List;

// OOP Concept: Abstraction - representing a real-world course blueprint
@Entity
@Table(name = "courses")
public class Course {

    // OOP Concept: Encapsulation - private fields protect data
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    private int credits;

    private String lecturer;

    // OOP Concept: Association - many courses belong to one department
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // OOP Concept: Polymorphism - constructor overloading
    public Course() {}

    public Course(String courseName, String courseCode, int credits, String lecturer) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.lecturer = lecturer;
    }

    // OOP Concept: Encapsulation - getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}