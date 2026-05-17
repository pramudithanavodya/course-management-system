package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_username", nullable = false)
    private String studentUsername;

    /**
     * Types: LOGIN, ENROLLED, UNENROLLED, WELCOME
     */
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read")
    private boolean read = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(String studentUsername, String type, String message) {
        this.studentUsername = studentUsername;
        this.type            = type;
        this.message         = message;
        this.read            = false;
        this.createdAt       = LocalDateTime.now();
    }

    // ── Getters & Setters ──────────────────────────────────────
    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getStudentUsername()           { return studentUsername; }
    public void setStudentUsername(String u)     { this.studentUsername = u; }

    public String getType()                      { return type; }
    public void setType(String type)             { this.type = type; }

    public String getMessage()                   { return message; }
    public void setMessage(String message)       { this.message = message; }

    public boolean isRead()                      { return read; }
    public void setRead(boolean read)            { this.read = read; }

    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDateTime d)    { this.createdAt = d; }
}
