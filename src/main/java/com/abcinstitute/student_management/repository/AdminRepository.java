<<<<<<< HEAD
package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByUsernameAndPassword(String username, String password);
=======
package com.abcinstitute.student_management.repository;

import com.abcinstitute.student_management.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByUsernameAndPassword(String username, String password);
>>>>>>> 0331f75549f6d4f92c280c11ecd72798cdeffd65
}