package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dept_name", nullable = false)
    private String deptName;

    @Column(name = "dept_code", nullable = false, unique = true)
    private String deptCode;

    private String description;

    @Column(name = "head_of_dept")
    private String headOfDept;

    // OOP: One-to-Many relationship (1 dept → many courses)
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Course> courses;

    public Department() {}

    public Department(String deptName, String deptCode, String description, String headOfDept) {
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.description = description;
        this.headOfDept = headOfDept;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHeadOfDept() { return headOfDept; }
    public void setHeadOfDept(String headOfDept) { this.headOfDept = headOfDept; }

    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
}