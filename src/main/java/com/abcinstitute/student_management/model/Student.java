package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    // Default constructor
    public Student() {}

    // Parameterized constructor
    public Student(String username, String email, String phone,
                   String password, String fullName) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fullName = fullName;
        this.createdDate = LocalDateTime.now();
    }

    // Keep these for backward compatibility (viva - explain file to DB migration)
    public String toFileString() {
        return username + "|" + email + "|" + phone + "|" + password + "|" + fullName;
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 5) return null;
        return new Student(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Student{username='" + username + "', email='"
                + email + "', fullName='" + fullName + "'}";
    }
}