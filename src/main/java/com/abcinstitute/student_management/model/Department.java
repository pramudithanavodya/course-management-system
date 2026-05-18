
package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import java.util.List;

// OOP Concept: Abstraction - representing a real-world department blueprint
@Entity
@Table(name = "departments")
public class Department {

    // OOP Concept: Encapsulation - private fields protect data from direct access
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

    // OOP Concept: Association - department is linked to multiple courses
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Course> courses;

    // OOP Concept: Polymorphism - constructor overloading
    public Department() {}

    public Department(String deptName, String deptCode, String description, String headOfDept) {
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.description = description;
        this.headOfDept = headOfDept;
    }

    // OOP Concept: Encapsulation - getters and setters to access private fields
    
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