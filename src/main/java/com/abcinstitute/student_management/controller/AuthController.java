
package com.abcinstitute.student_management.controller;

import com.abcinstitute.student_management.model.Student;
import com.abcinstitute.student_management.service.AdminService;
import com.abcinstitute.student_management.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    // ========================
    // STUDENT LOGIN
    // ========================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (studentService.authenticate(username, password)) {
            session.setAttribute("loggedInUser", username);
            session.setAttribute("userType", "STUDENT");
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password!");
            return "redirect:/login";
        }
    }

    // ========================
    // STUDENT SIGNUP
    // ========================
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String fullName,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
            return "redirect:/signup";
        }

        Student student = new Student(username, email, phone, password, fullName);
        boolean registered = studentService.registerStudent(student);

        if (registered) {
            redirectAttributes.addFlashAttribute("success",
                    "Account created! Please login.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Username already taken. Choose another.");
            return "redirect:/signup";
        }
    }

    // ========================
    // ADMIN LOGIN
    // ========================
    @GetMapping("/admin-login")
    public String adminLoginPage() {
        return "admin-login";
    }

    @PostMapping("/admin-login")
    public String processAdminLogin(@RequestParam String username,
                                    @RequestParam String password,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (adminService.authenticate(username, password)) {
            session.setAttribute("loggedInUser", username);
            session.setAttribute("userType", "ADMIN");
            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid admin credentials!");
            return "redirect:/admin-login";
        }
    }

    // ========================
    // LOGOUT
    // ========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}