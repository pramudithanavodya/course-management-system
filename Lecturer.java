package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Lecturer entity — maps to the "lecturers" table.
 * JPA handles all INSERT / UPDATE / DELETE automatically via the repository.
 */
@Entity
@Table(name = "lecturers")
public class Lecturer {

    // Auto-incremented primary key — never set this manually
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Lecturer name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(name = "lecturer_name", nullable = false, length = 100)
    private String lecturerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // Optional field — no @NotBlank; allows international formats e.g. "+94 77 123 4567"
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    @Column(length = 15)
    private String phone;

    // Many lecturers → one department; stores department_id as a foreign key in this table
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // No-arg constructor required by JPA
    public Lecturer() {}

    public Lecturer(String lecturerName, String email, String phone, Department department) {
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

    // Department excluded to avoid lazy-loading issues outside a transaction
    @Override
    public String toString() {
        return "Lecturer{id=" + id +
                ", lecturerName='" + lecturerName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' + '}';
    }
}
