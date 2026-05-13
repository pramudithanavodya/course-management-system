package com.abcinstitute.student_management.controller;

import com.abcinstitute.student_management.model.Course;
import com.abcinstitute.student_management.service.CourseService;
import com.abcinstitute.student_management.service.EnrollmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    // Student Dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        List<Course> allCourses = courseService.getAllCourses();
        List<Course> enrolledCourses = enrollmentService.getEnrolledCourses(username);

        model.addAttribute("username", username);
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "dashboard";
    }

    // Enroll Page
    @GetMapping("/enroll/{courseId}")
    public String enrollPage(@PathVariable Long courseId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) return "redirect:/dashboard";

        boolean alreadyEnrolled = enrollmentService.isEnrolled(username, courseId);
        model.addAttribute("course", course.get());
        model.addAttribute("alreadyEnrolled", alreadyEnrolled);
        return "enroll";
    }

    // Process Enrollment
    @PostMapping("/enroll/{courseId}")
    public String processEnrollment(@PathVariable Long courseId,
                                    @RequestParam String enrollmentKey,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        String result = enrollmentService.enrollStudent(username, courseId, enrollmentKey);

        switch (result) {
            case "SUCCESS":
                redirectAttributes.addFlashAttribute("success", "Enrolled successfully! Welcome to the course.");
                return "redirect:/course-content/" + courseId;
            case "ALREADY_ENROLLED":
                return "redirect:/course-content/" + courseId;
            case "WRONG_KEY":
                redirectAttributes.addFlashAttribute("error",
                        "Incorrect enrollment key. Please check your payment receipt.");
                return "redirect:/enroll/" + courseId;
            default:
                redirectAttributes.addFlashAttribute("error", "Something went wrong.");
                return "redirect:/dashboard";
        }
    }

    // Course Content (after enrollment)
    @GetMapping("/course-content/{courseId}")
    public String courseContent(@PathVariable Long courseId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        if (!enrollmentService.isEnrolled(username, courseId)) {
            return "redirect:/enroll/" + courseId;
        }

        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) return "redirect:/dashboard";

        model.addAttribute("course", course.get());
        model.addAttribute("username", username);
        return "course-content";
    }
}