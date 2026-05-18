
package com.abcinstitute.student_management.service;

import com.abcinstitute.student_management.model.Admin;
import com.abcinstitute.student_management.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Seed the super-admin on first start.
     * If the "admin" user already exists but has the wrong role, correct it to SUPER_ADMIN.
     */
    @PostConstruct
    public void seedSuperAdmin() {
        Optional<Admin> existing = adminRepository.findByUsername("admin");
        if (existing.isEmpty()) {
            // First run: create the super admin fresh
            Admin superAdmin = new Admin("admin", "admin123", "admin@abcinstitute.com", "SUPER_ADMIN");
            adminRepository.save(superAdmin);
        } else {
            // Already exists: make sure the role is SUPER_ADMIN (fix any stale data)
            Admin admin = existing.get();
            if (!"SUPER_ADMIN".equals(admin.getRole())) {
                admin.setRole("SUPER_ADMIN");
                adminRepository.save(admin);
            }
        }
    }

    public boolean authenticate(String username, String password) {
        Optional<Admin> admin = adminRepository.findByUsernameAndPassword(username, password);
        return admin.isPresent();
    }

    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    /** Returns "SUPER_ADMIN" or "ADMIN" for the given user, or null if not found. */
    public String getRoleByUsername(String username) {
        return adminRepository.findByUsername(username)
                .map(Admin::getRole)
                .orElse(null);
    }

    /** All regular admins (excludes super admin) */
    public List<Admin> getAllRegularAdmins() {
        return adminRepository.findByRole("ADMIN");
    }

    /** All admins including super admin */
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    /**
     * Save a new regular admin. Returns false if username already taken.
     */
    public boolean saveAdmin(Admin admin) {
        if (adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            return false;
        }
        admin.setRole("ADMIN"); // always ADMIN for new ones created via panel
        adminRepository.save(admin);
        return true;
    }

    /**
     * Update an existing admin (cannot change the super admin's role or delete it).
     */
    public boolean updateAdmin(Long id, String newUsername, String newPassword, String newEmail) {
        Optional<Admin> opt = adminRepository.findById(id);
        if (opt.isEmpty()) return false;
        Admin admin = opt.get();

        // Check username uniqueness if changed
        if (!admin.getUsername().equals(newUsername) &&
            adminRepository.findByUsername(newUsername).isPresent()) {
            return false;
        }

        admin.setUsername(newUsername);
        admin.setEmail(newEmail);
        if (newPassword != null && !newPassword.isBlank()) {
            admin.setPassword(newPassword);
        }
        // Role is always preserved — never change it here
        adminRepository.save(admin);
        return true;
    }

    /**
     * Delete admin by id. Prevents deletion of super admin (id == 1 or role == SUPER_ADMIN).
     */
    public boolean deleteAdmin(Long id) {
        Optional<Admin> opt = adminRepository.findById(id);
        if (opt.isEmpty()) return false;
        if (opt.get().isSuperAdmin()) return false;
        adminRepository.deleteById(id);
        return true;
    }
}
