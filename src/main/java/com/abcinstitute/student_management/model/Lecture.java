package com.abcinstitute.student_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecturer_name", nullable = false)
    private String lecturerName;

    private String email;
    private String phone;

    // OOP: Many-to-One (many lecturers belong to one department)
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Lecture() {}

    public Lecture(String lecturerName, String email, String phone, Department department) {
        this.lecturerName = lecturerName;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    // Getters and Setters
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