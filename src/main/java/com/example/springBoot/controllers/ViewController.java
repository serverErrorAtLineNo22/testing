package com.example.springBoot.controllers;

import com.example.springBoot.daos.CourseDao;
import com.example.springBoot.daos.StudentDao;
import com.example.springBoot.daos.UserDao;
import com.example.springBoot.models.Course;
import com.example.springBoot.models.Student;
import com.example.springBoot.models.User;
import com.example.springBoot.services.CourseService;
import com.example.springBoot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/view")
public class ViewController {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserDao userDao;

    @GetMapping("/StudentView")
    public String studentView(Model model){
        int latestCourseId = courseService.getLatestCourseId();
        String stringCourseId = courseService.generateStringCourseId(latestCourseId);
        List<Student> students = studentDao.findAll();
        for (Student student : students) {
            byte[] photoBytes = student.getPhoto();
            String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
            student.setBase64Photo(base64Photo);
        }
        model.addAttribute("students",students);
        model.addAttribute("course", new Course());
        model.addAttribute("stringCourseId", stringCourseId);
        return "View/studentView";
    }
    @GetMapping("/UserView")
    public String userView(Model model){
        int latestCourseId = courseService.getLatestCourseId();
        String stringCourseId = courseService.generateStringCourseId(latestCourseId);
        List<User> users = userDao.findAll();
        for (User user : users){
            List<Course> courses = courseDao.findByUserId(user.getId()); // Assuming you have a method to fetch courses by user ID
            user.setCourses(courses);
            byte[] photoBytes = user.getPhoto();
            if (photoBytes != null) {
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                user.setBase64Photo(base64Photo);
            }
        }
        model.addAttribute("users",users);
        model.addAttribute("course", new Course());
        model.addAttribute("stringCourseId", stringCourseId);
        return "View/userView";
    }
    @GetMapping("/CourseView")
    public String courseView(Model model){
        int latestCourseId = courseService.getLatestCourseId();
        String stringCourseId = courseService.generateStringCourseId(latestCourseId);
        List<Course> courses = courseDao.findAll();
        for (Course course : courses) {
            String stringCourse_Id = courseService.getStringCourseId(course.getId());
            course.setStringCourse_id(stringCourse_Id);
        }
        model.addAttribute("stringCourseId", stringCourseId);
        model.addAttribute("courses",courses);
        model.addAttribute("course", new Course());
        return "View/courseView";
    }
    @GetMapping("/viewStudentDetail/{id}")
    public String getStudentDetails(@PathVariable int id, Model model) {
        Student student = studentDao.findById(id).orElse(null);
        model.addAttribute("student", student);
        return "/View/student-details :: detailsFragment";
    }
    @PostMapping("/search")
    public String searchStudents(@RequestParam("searchQuery") String searchQuery, Model model) {
        List<Student> students = new ArrayList<>();

        try {
            int studentId = Integer.parseInt(searchQuery);
            Optional<Student> studentById = studentDao.findById(studentId);
            studentById.ifPresent(students::add);
        } catch (NumberFormatException e) {

        }


        List<Student> studentsByName = studentDao.findByNameContainingIgnoreCase(searchQuery);
        students.addAll(studentsByName);


        List<Student> studentsByCourseName = studentDao.findByCoursesCourseNameContainingIgnoreCase(searchQuery);
        students.addAll(studentsByCourseName);
        for (Student student : students) {
            byte[] photoBytes = student.getPhoto();
            String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
            student.setBase64Photo(base64Photo);
        }
        model.addAttribute("students", students);
        return "View/studentView";
    }
    @PostMapping("/UserSearch")
    public String searchUsers(@RequestParam("searchQuery") String searchQuery, Model model) {
        List<User> users = new ArrayList<>();

        try {
            int userId = Integer.parseInt(searchQuery);
            Optional<User> userById = userDao.findById(userId);
            userById.ifPresent(users::add);
        } catch (NumberFormatException e) {
            // Handle if the search query is not a valid user ID
        }

        // Search users by name
        List<User> usersByName = userDao.findByNameContainingIgnoreCase(searchQuery);
        users.addAll(usersByName);

        // Search users by email
        List<User> usersByEmail = userDao.findByEmailContainingIgnoreCase(searchQuery);
        users.addAll(usersByEmail);

        for (User user : users) {
            byte[] photoBytes = user.getPhoto();
            if (photoBytes != null) {
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                user.setBase64Photo(base64Photo);
            }
        }

        model.addAttribute("users", users);
        return "View/userView";
    }
}
