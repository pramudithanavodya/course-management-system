
package com.abcinstitute.student_management.controller;

import com.abcinstitute.student_management.model.*;
import com.abcinstitute.student_management.repository.*;
import com.abcinstitute.student_management.service.AdminService;
import com.abcinstitute.student_management.service.CourseService;
import com.abcinstitute.student_management.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private CourseService courseService;
    @Autowired private StudentService studentService;
    @Autowired private AdminService adminService;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private LectureRepository lectureRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;

    // ─── Guard: check admin session ───
    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userType"));
    }
    
    // ─── Guard: check super-admin ───
    private boolean isSuperAdmin(HttpSession session) {
        return "SUPER_ADMIN".equals(session.getAttribute("adminRole"));
    }

    /** Injects adminRole into the model so all templates can conditionally render the Admins nav link */
    private void addCommonAttributes(HttpSession session, Model model) {
        model.addAttribute("adminRole", session.getAttribute("adminRole"));
        model.addAttribute("adminName", session.getAttribute("loggedInUser"));
    }

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        model.addAttribute("totalStudents", studentService.getAllStudents().size());
        model.addAttribute("totalDepts", departmentRepository.count());
        model.addAttribute("totalLecturers", lectureRepository.count());
        addCommonAttributes(session, model);
        return "admin/admin-dashboard";
    }

    // ─────────────── COURSES ───────────────
    @GetMapping("/courses")
    public String listCourses(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("courses", courseService.getAllCourses());
        addCommonAttributes(session, model);
        return "admin/courses";
    }

    @GetMapping("/courses/add")
    public String addCourseForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("course", new Course());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("lecturers", lectureRepository.findAll());
        addCommonAttributes(session, model);
        return "admin/add-course";
    }

    @PostMapping("/courses/add")
    public String saveCourse(@ModelAttribute Course course,
                             @RequestParam Long departmentId,
                             @RequestParam(required = false) Long lecturerId,
                             HttpSession session,
                             RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        departmentRepository.findById(departmentId).ifPresent(course::setDepartment);
        if (lecturerId != null) lectureRepository.findById(lecturerId).ifPresent(course::setLecturer);
        courseService.saveCourse(course);
        ra.addFlashAttribute("success", "Course added successfully!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourseForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isEmpty()) return "redirect:/admin/courses";
        model.addAttribute("course", course.get());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("lecturers", lectureRepository.findAll());
        addCommonAttributes(session, model);
        return "admin/edit-course";
    }

    @PostMapping("/courses/edit/{id}")
    public String updateCourse(@PathVariable Long id,
                               @ModelAttribute Course course,
                               @RequestParam Long departmentId,
                               @RequestParam(required = false) Long lecturerId,
                               HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        course.setId(id);
        departmentRepository.findById(departmentId).ifPresent(course::setDepartment);
        if (lecturerId != null) lectureRepository.findById(lecturerId).ifPresent(course::setLecturer);
        courseService.updateCourse(course);
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }

    // ─────────────── LECTURERS ───────────────
    @GetMapping("/lectures")
    public String listLecturers(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("lecturers", lectureRepository.findAll());
        addCommonAttributes(session, model);
        return "admin/lectures";
    }

    @GetMapping("/lectures/add")
    public String addLecturerForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("lecturer", new Lecture());
        model.addAttribute("departments", departmentRepository.findAll());
        addCommonAttributes(session, model);
        return "admin/add-lecture";
    }

    @PostMapping("/lectures/add")
    public String saveLecturer(@ModelAttribute Lecture lecturer,
                               @RequestParam Long departmentId,
                               HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        departmentRepository.findById(departmentId).ifPresent(lecturer::setDepartment);
        lectureRepository.save(lecturer);
        return "redirect:/admin/lectures";
    }

    @GetMapping("/lectures/delete/{id}")
    public String deleteLecturer(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        lectureRepository.deleteById(id);
        return "redirect:/admin/lectures";
    }

    // ─────────────── DEPARTMENTS ───────────────
    @GetMapping("/departments")
    public String listDepts(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("departments", departmentRepository.findAll());
        addCommonAttributes(session, model);
        return "admin/departments";
    }

    @PostMapping("/departments/add")
    public String addDept(@RequestParam String deptName,
                          @RequestParam String deptCode,
                          @RequestParam String description,
                          @RequestParam String headOfDept,
                          HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        Department dept = new Department(deptName, deptCode, description, headOfDept);
        departmentRepository.save(dept);
        return "redirect:/admin/departments";
    }

    @GetMapping("/departments/delete/{id}")
    public String deleteDept(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        departmentRepository.deleteById(id);
        return "redirect:/admin/departments";
    }

    // ─────────────── STUDENTS (read from file) ───────────────
    @GetMapping("/students")
    public String listStudents(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("students", studentService.getAllStudents());
        addCommonAttributes(session, model);
        return "admin/students";
    }

        // ─────────────── EDIT LECTURER ───────────────
    @GetMapping("/lectures/edit/{id}")
    public String editLecturerForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        Lecture lecture = lectureRepository.findById(id).orElse(null);
        if (lecture == null) return "redirect:/admin/lectures";
        model.addAttribute("lecture", lecture);
        model.addAttribute("departments", departmentRepository.findAll());
        addCommonAttributes(session, model);
        return "admin/edit-lecture";
    }

    @PostMapping("/lectures/edit/{id}")
    public String updateLecturer(@PathVariable Long id,
                                 @RequestParam String lecturerName,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam Long departmentId,
                                 HttpSession session,
                                 RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        Lecture lecture = lectureRepository.findById(id).orElse(null);
        if (lecture == null) return "redirect:/admin/lectures";
        lecture.setLecturerName(lecturerName);
        lecture.setEmail(email);
        lecture.setPhone(phone);
        departmentRepository.findById(departmentId).ifPresent(lecture::setDepartment);
        lectureRepository.save(lecture);
        ra.addFlashAttribute("success", "Lecturer updated successfully.");
        return "redirect:/admin/lectures";
    }

    // ─────────────── DEPARTMENTS EDIT ───────────────
    @GetMapping("/departments/edit/{id}")
    public String editDeptForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        Department dept = departmentRepository.findById(id).orElse(null);
        if (dept == null) return "redirect:/admin/departments";
        model.addAttribute("department", dept);
        addCommonAttributes(session, model);
        return "admin/edit-department";
    }

    @PostMapping("/departments/edit/{id}")
    public String updateDept(@PathVariable Long id,
                             @RequestParam String deptName,
                             @RequestParam String deptCode,
                             @RequestParam String description,
                             @RequestParam String headOfDept,
                             HttpSession session,
                             RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        Department dept = departmentRepository.findById(id).orElse(null);
        if (dept == null) return "redirect:/admin/departments";
        dept.setDeptName(deptName);
        dept.setDeptCode(deptCode);
        dept.setDescription(description);
        dept.setHeadOfDept(headOfDept);
        departmentRepository.save(dept);
        ra.addFlashAttribute("success", "Department updated successfully.");
        return "redirect:/admin/departments";
    }

    @GetMapping("/departments/add")
    public String addDeptForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        addCommonAttributes(session, model);
        return "admin/add-department";
    }

    // ─────────────── STUDENTS EDIT / DELETE ───────────────
    @GetMapping("/students/delete/{username}")
    public String deleteStudent(@PathVariable String username, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        boolean deleted = studentService.deleteStudent(username);
        if (deleted) {
            ra.addFlashAttribute("success", "Student '" + username + "' has been removed.");
        } else {
            ra.addFlashAttribute("error", "Unable to delete student. They may not exist.");
        }
        return "redirect:/admin/students";
    }

    @GetMapping("/students/edit/{username}")
    public String editStudentForm(@PathVariable String username, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        Student student = studentService.findByUsername(username).orElse(null);
        if (student == null) return "redirect:/admin/students";
        model.addAttribute("student", student);
        addCommonAttributes(session, model);
        return "admin/edit-student";
    }

    @PostMapping("/students/edit/{username}")
    public String updateStudent(@PathVariable String username,
                                @RequestParam String fullName,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam(required = false) String newPassword,
                                @RequestParam(required = false) String confirmPassword,
                                HttpSession session,
                                RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        Student student = studentService.findByUsername(username).orElse(null);
        if (student == null) return "redirect:/admin/students";
        student.setFullName(fullName);
        student.setEmail(email);
        student.setPhone(phone);
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                ra.addFlashAttribute("error", "Passwords do not match!");
                return "redirect:/admin/students/edit/" + username;
            }
            student.setPassword(newPassword);
        }
        studentService.updateStudent(student);
        ra.addFlashAttribute("success", "Student '" + username + "' updated successfully.");
        return "redirect:/admin/students";
    }

    // ═══════════════════════════════════════════════════════════════
    // ─────────────── ADMINS MANAGEMENT (Super Admin Only) ──────────
    // ═══════════════════════════════════════════════════════════════

    @GetMapping("/admins")
    public String listAdmins(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        if (!isSuperAdmin(session)) return "redirect:/admin/dashboard";
        model.addAttribute("admins", adminService.getAllAdmins());
        addCommonAttributes(session, model);
        return "admin/admins";
    }

    @GetMapping("/admins/add")
    public String addAdminForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        if (!isSuperAdmin(session)) return "redirect:/admin/dashboard";
        addCommonAttributes(session, model);
        return "admin/add-admin";
    }

    @PostMapping("/admins/add")
    public String saveAdmin(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String email,
                            HttpSession session,
                            RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        if (!isSuperAdmin(session)) return "redirect:/admin/dashboard";
        Admin newAdmin = new Admin(username, password, email, "ADMIN");
        boolean saved = adminService.saveAdmin(newAdmin);
        if (saved) {
            ra.addFlashAttribute("success", "Admin '" + username + "' created successfully!");
        } else {
            ra.addFlashAttribute("error", "Username already taken. Choose another.");
        }
        return "redirect:/admin/admins";
    }

    @GetMapping("/admins/edit/{id}")
    public String editAdminForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        if (!isSuperAdmin(session)) return "redirect:/admin/dashboard";
        Admin admin = adminService.findById(id).orElse(null);
        if (admin == null) return "redirect:/admin/admins";
        // Super admin CAN be edited (but not deleted) — no redirect here
        model.addAttribute("adminUser", admin);
        addCommonAttributes(session, model);
        return "admin/edit-admin";
    }

    @PostMapping("/admins/edit/{id}")
    public String updateAdmin(@PathVariable Long id,
                              @RequestParam String username,
                              @RequestParam(required = false) String newPassword,
                              @RequestParam String email,
                              HttpSession session,
                              RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        if (!isSuperAdmin(session)) return "redirect:/admin/dashboard";
        boolean updated = adminService.updateAdmin(id, username, newPassword, email);
        if (updated) {
            ra.addFlashAttribute("success", "Admin updated successfully.");
        } else {
            ra.addFlashAttribute("error", "Could not update admin (username may already be taken).");
        }
        return "redirect:/admin/admins";
    }

    @GetMapping("/admins/delete/{id}")
    public String deleteAdmin(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        if (!isSuperAdmin(session)) return "redirect:/admin/dashboard";
        boolean deleted = adminService.deleteAdmin(id);
        if (deleted) {
            ra.addFlashAttribute("success", "Admin deleted successfully.");
        } else {
            ra.addFlashAttribute("error", "Cannot delete this admin (may be super admin).");
        }
        return "redirect:/admin/admins";
    }
}

