package com.example.springBoot.controllers;


import com.example.springBoot.daos.CourseDao;
import com.example.springBoot.models.Course;
import com.example.springBoot.models.User;
import com.example.springBoot.services.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/course")
public class courseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseDao courseDao;
    @PostMapping("/addCourse")
    public String addCourse(@ModelAttribute Course course, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        int id = (int) session.getAttribute("id");
        User user = new User();
        user.setId(id);
        course.setUser(user);
        courseDao.save(course);
        return "redirect:/user/home";
    }
}

