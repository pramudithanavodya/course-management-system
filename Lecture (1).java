package com.abcinstitute.student_management.model;

import jakarta.persistence.*;

// Marks this class as a JPA entity mapped to the "lectures" table in the database
@Entity
@Table(name = "lectures")
public class Lecture {

    // Primary key — auto-incremented by the database, never set this manually
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Maps to the "lecturer_name" column; cannot be null in the database
    @Column(name = "lecturer_name", nullable = false)
    private String lecturerName;

    // No column constraints — these are optional fields
    private String email;
    private String phone;

    // Many-to-One: many lecturers can belong to one department
    // Stores department_id as a foreign key in this table
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // No-arg constructor required by JPA to load entities from the database
    public Lecture() {}

    // Parameterised constructor — use this when creating a new Lecture in code
    public Lecture(String lecturerName, String email, String phone, Department department) {
        this.lecturerName = lecturerName;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    // Getters and Setters — used by Spring, JPA, and Jackson to read/write field values
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String lecturerName) { this.lecturerName = lecturerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
