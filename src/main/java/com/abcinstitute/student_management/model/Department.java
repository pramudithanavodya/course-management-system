
package com.abcinstitute.student_management.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * OOP CONCEPT: ABSTRACTION & ENCAPSULATION
 * - Abstraction: The Department class acts as a blueprint/abstraction representing a real-world department,
 *   focusing on relevant properties (id, name, code, head, courses) and hiding the database mapping details.
 * - Encapsulation: All properties are declared 'private' to restrict direct modification. Access is controlled
 *   entirely through public getter and setter methods.
 */
@Entity
@Table(name = "departments")
public class Department {

    // OOP CONCEPT: ENCAPSULATION (Data Hiding)
    // Declaring properties as private to hide the internal state of the object.
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

    // OOP CONCEPT: ASSOCIATION (One-to-Many Relationship)
    // A Department is associated with multiple Course objects, establishing a structural relationship between objects.
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Course> courses;

    // OOP CONCEPT: POLYMORPHISM (Constructor Overloading)
    // Having a default constructor alongside a parameterized constructor is an example of constructor overloading (Compile-time Polymorphism).
    public Department() {}

    public Department(String deptName, String deptCode, String description, String headOfDept) {
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.description = description;
        this.headOfDept = headOfDept;
    }

    // OOP CONCEPT: ENCAPSULATION (Accessors and Mutators)
    // Public getters and setters provide safe, controlled access and modification of the private fields.
    
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