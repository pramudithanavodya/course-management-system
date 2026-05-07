package com.abcinstitute.student_management.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Landing page → http://localhost:8080/
    @GetMapping("/")
    public String home() {
        return "index"; // loads templates/index.html
    }
}
