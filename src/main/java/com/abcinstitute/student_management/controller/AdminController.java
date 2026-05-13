package com.abcinstitute.student_management.controller;

import com.abcinstitute.student_management.model.*;
import com.abcinstitute.student_management.repository.*;
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
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private LectureRepository lectureRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;

    // ─── Guard: check admin session ───
    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userType"));
    }

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        model.addAttribute("totalStudents", studentService.getAllStudents().size());
        model.addAttribute("totalDepts", departmentRepository.count());
        model.addAttribute("totalLecturers", lectureRepository.count());
        model.addAttribute("adminName", session.getAttribute("loggedInUser"));
        return "admin/admin-dashboard";
    }

    // ─────────────── COURSES ───────────────
    @GetMapping("/courses")
    public String listCourses(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses";
    }

    @GetMapping("/courses/add")
    public String addCourseForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("course", new Course());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("lecturers", lectureRepository.findAll());
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
        return "admin/lectures";
    }

    @GetMapping("/lectures/add")
    public String addLecturerForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("lecturer", new Lecture());
        model.addAttribute("departments", departmentRepository.findAll());
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
        return "admin/students";
    }
}