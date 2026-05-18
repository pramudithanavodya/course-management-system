
package com.abcinstitute.student_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_users")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    /**
     * Role: "SUPER_ADMIN" or "ADMIN"
     */
    @Column(nullable = false)
    private String role = "ADMIN";

    public Admin() {}

    public Admin(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "ADMIN";
    }

    public Admin(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isSuperAdmin() { return "SUPER_ADMIN".equals(this.role); }

}


