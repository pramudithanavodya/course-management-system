package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Marks this interface as a Spring-managed repository bean
@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    // Extends JpaRepository which provides built-in methods out of the box:
    // save(), findById(), findAll(), deleteById(), count(), and more —
    // all without writing any SQL or implementation code

    // JpaRepository<Lecture, Long>:
    //   Lecture → the entity this repository manages
    //   Long    → the data type of the primary key (id)

    // Custom query method — Spring Data JPA automatically generates the SQL
    // based on the method name: SELECT * FROM lectures WHERE department_id = ?
    List<Lecture> findByDepartmentId(Long departmentId);
}
