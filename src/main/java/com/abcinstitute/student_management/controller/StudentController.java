
package com.abcinstitute.student_management.controller;

import com.abcinstitute.student_management.model.Course;
import com.abcinstitute.student_management.model.Student;
import com.abcinstitute.student_management.service.*;
import jakarta.servlet.http.HttpSession; /// I want to use the HttpSession class
                                         /// from the Jakarta Servlet library
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; /// Model = sends data to HTML pages
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
    import java.util.Optional;

@Controller  /// @Controller is a Spring MVC annotation that tells Spring:
             /// This class is responsible for handling web requests and returning web pages

public class StudentController {
    @Autowired
    private StudentService studentService;


        @Autowired private CourseService courseService;
        @Autowired private EnrollmentService enrollmentService;




    @GetMapping("/dashboard")  /// When someone visits /dashboard, run this method.
    public String dashboard(HttpSession httpsession, Model model) {

        ///  Who is currently logged in (stored in httpsession)
        String username = (String) httpsession.getAttribute("loggedInUser");

        ///  If not logged in → send to login page
        if (username == null) return "redirect:/login";

        /// "model" is an interface in Spring MVC.
        /// It is used to: Carry data from Java (backend) → HTML page (frontend)
        /// addAttribute() is a method inside the model interface Provided by Spring
        /// Store data in Model using a key-value pair

        model.addAttribute("username", username);

        /// Go to courseService.java and get all courses from database (or list)
        model.addAttribute("allCourses", courseService.getAllCourses());

        /// Go to enrollmentService.java and Get only the courses this specific user enrolled in
        model.addAttribute("enrolledCourses", enrollmentService.getEnrolledCourses(username));

        return "dashboard";
    }




    @GetMapping("/enroll/{courseId}")   ///  Runs this method when user visits: /enroll/1
                                        ///  {courseId} = dynamic value from URL.

    /// @PathVariable --> Takes value from URL and puts it into Java variable.

            /// HttpSession httpsession ---> What it means:
           /// 👉“Create a variable called httpsession of type HttpSession” --> So:
          /// HttpSession = TYPE (class)
        /// httpsession = VARIABLE (object reference)

    public String enrollPage(@PathVariable Long courseId,
                             HttpSession httpsession, Model model) {


        ///  (String) = Type casting
        ///  Because this part:  httpsession.getAttribute("loggedInUser")
        ///  is generic type, not specific (A generic type in Java means: ///
        /// “A type that is NOT fixed yet — it can hold ANY type.”)

        ///  So we tell Java: (String)
        /// “Treat this value as a String”

                             ///  httpsession = session object
                            /// (A storage area for user data on the server)
                          ///  It keeps thing like : logged-in user , login status , login info

            /// ✔ .getAttribute() ----->  This is a method of HttpSession
            /// Meaning:“Get a value stored in session using a key”

                        /// "loggedInUser"
                       /// This is the key (name) used to store data in session -->
                       /// Key: loggedInUser	Value: John

        String username = (String) httpsession.getAttribute("loggedInUser");

        /// If no user is logged in, stop everything and send them to the login page
        if (username == null) return "redirect:/login";




        /// Optional means= This value may exist or may not exist.
        ///  Why Java created Optional ---> To reduce problems caused by: null
        /// because null often causes crashes like:NullPointerException

        /// <Course> = This Optional box stores a Course object
        ///  course = Variable name
        /// Go to CourseService.java and Get  the course this specific courseID
        Optional<Course> course = courseService.getCourseById(courseId);

        /// If course does not exist, send user back to dashboard.
        if (course.isEmpty()) return "redirect:/dashboard";




        model.addAttribute("course", course.get());
        model.addAttribute("alreadyEnrolled", enrollmentService.isEnrolled(username, courseId));

        return "enroll";
    }

    @PostMapping("/enroll/{courseId}")
    public String processEnrollment(@PathVariable Long courseId,
                                    @RequestParam String enrollmentKey,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        switch (enrollmentService.enrollStudent(username, courseId, enrollmentKey)) {

            case "SUCCESS":
                redirectAttributes.addFlashAttribute("success",
                        "Enrolled successfully!");
                return "redirect:/course-content/" + courseId;

            case "ALREADY_ENROLLED":
                return "redirect:/course-content/" + courseId;

            case "WRONG_KEY":
                redirectAttributes.addFlashAttribute("error",
                        "Incorrect enrollment key");
                return "redirect:/enroll/" + courseId;

            default:
                redirectAttributes.addFlashAttribute("error",
                        "Something went wrong");
                return "redirect:/dashboard";
        }
    }

    @GetMapping("/course-content/{courseId}")
    public String courseContent(@PathVariable Long courseId,
                                HttpSession session,
                                Model model) {

        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        if (!enrollmentService.isEnrolled(username, courseId))
            return "redirect:/enroll/" + courseId;

        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) return "redirect:/dashboard";

        model.addAttribute("course", course.get());
        model.addAttribute("username", username);

        return "course-content";
    }


    @GetMapping("/my-courses")
    public String myCourses(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        List<Course> enrolledCourses = enrollmentService.getEnrolledCourses(username);

        model.addAttribute("username", username);
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "my-courses";  // loads templates/my-courses.html
    }

    // GET: Show settings page pre-filled with current student data
    @GetMapping("/settings")
    public String settingsPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        // Fetch student from JPA repository
        Student student = studentService.findByUsername(username).orElse(null);
        if (student == null) return "redirect:/login";

        model.addAttribute("username", username);
        model.addAttribute("student", student);
        return "settings";
    }

    // POST: Process settings update
    @PostMapping("/settings")
    public String updateSettings(@RequestParam String fullName,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam(required = false) String newPassword,
                                 @RequestParam(required = false) String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return "redirect:/login";

        // Fetch current student from DB
        Student student = studentService.findByUsername(username).orElse(null);
        if (student == null) return "redirect:/login";

        // If new password provided, validate match
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                return "redirect:/settings";
            }
            student.setPassword(newPassword); // will be saved only if not empty (service handles that)
        }

        // Update other fields
        student.setFullName(fullName);
        student.setEmail(email);
        student.setPhone(phone);

        // Call service update
        boolean updated = studentService.updateStudent(student);
        if (updated) {
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Update failed. Please try again.");
        }

        return "redirect:/settings";
    }



}
