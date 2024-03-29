package com.example.springBoot.controllers;

import com.example.springBoot.daos.CourseDao;
import com.example.springBoot.daos.EnrollDao;
import com.example.springBoot.daos.StudentDao;
import com.example.springBoot.models.Course;
import com.example.springBoot.models.Enroll;
import com.example.springBoot.models.Student;
import com.example.springBoot.services.EnrollService;
import com.example.springBoot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private EnrollDao enrollDao;
    @Autowired
    private EnrollService enrollService;
@GetMapping("/home")
public String homePage(Model model){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String name = auth.getName();
    Student student = studentDao.findByName(name);
    String stringStudentId = studentService.getTheStringStudentId(student.getId());
    String base64Photo = Base64.getEncoder().encodeToString(student.getPhoto());
    List<Course> allCourses =courseDao.findAll();
    Set<Integer> enrolledCourseIds = new HashSet<>();
    for (Enroll enroll : student.getCourses()) {
        enrolledCourseIds.add(enroll.getCourse().getId());
    }
    model.addAttribute("student", student);
    model.addAttribute("stringStudentId", stringStudentId);
    model.addAttribute("base64Photo", base64Photo);
    model.addAttribute("allCourses",allCourses);
    model.addAttribute("enrolledCourseIds", enrolledCourseIds);
    return "Student/studentHome";
}
    @GetMapping("/register")
    public String showStudentRegisterPage(Model model){
        List<Course> allCourses =courseDao.findAll();
        int latestStudentId=studentService.findLatestStudentId();
        String stringStudentId=studentService.generateStringStudentId(latestStudentId);
        model.addAttribute("allCourses",allCourses);
        model.addAttribute("student",new Student());
        model.addAttribute("stringStudentId",stringStudentId);
        return "Student/studentRegister";
    }

    @PostMapping("/registration")
    public String addStudent(@ModelAttribute Student student,@RequestParam("courses") List<Integer> courseIds, RedirectAttributes redirectAttributes) {
        if (studentDao.findByName(student.getName()) == null && studentDao.findByEmail(student.getEmail()) == null) {
            Set<Enroll> enrollments = new HashSet<>();
            for (int courseId : courseIds) {
                Course course = courseDao.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + courseId));
                Enroll enroll = new Enroll();
                enroll.setStudent(student);
                enroll.setCourse(course);
                enroll.setEnroll_date(LocalDateTime.now());
                enrollments.add(enroll);
            }

            student.setCourses(enrollments);

            MultipartFile photo = student.getFile();
            if (photo != null && !photo.isEmpty()) {
                try {
                    byte[] photoBytes = photo.getBytes();
                    student.setPhoto(photoBytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            studentService.createStudent(student);
            return "redirect:/student/home";

        } else if (studentDao.findByEmail(student.getEmail()) != null) {
            redirectAttributes.addAttribute("emailExit", true);
            return "redirect:/student/register";
        } else if (studentDao.findByName(student.getName()) != null) {
            redirectAttributes.addAttribute("studentNameExist", true);
            return "redirect:/student/register";
        } else {
            redirectAttributes.addAttribute("emailExit", true);
            redirectAttributes.addAttribute("studentNameExist", true);
            return "redirect:/student/register";
        }
    }
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/update")
    public String updateStudent(@ModelAttribute("student") Student updatedStudent,
                                @RequestParam("courses") List<Integer> courseIds) {
        Student existingStudent = studentDao.findById(updatedStudent.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + updatedStudent.getId()));

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setDob(updatedStudent.getDob());
        existingStudent.setGender(updatedStudent.getGender());
        existingStudent.setPhone(updatedStudent.getPhone());
        existingStudent.setEducation(updatedStudent.getEducation());
        existingStudent.setEmail(updatedStudent.getEmail());
        MultipartFile photo = updatedStudent.getFile();
        try {
            if (!photo.isEmpty()) {
                byte[] photoBytes = photo.getBytes();
                existingStudent.setPhoto(photoBytes);
            }
        } catch (IOException ignored) {

        }
        List<Enroll> enrolls = enrollService.findOneByStudentId(updatedStudent.getId());
        enrollDao.deleteAllInBatch(enrolls);
        Set<Enroll> updatedCourses = new HashSet<>();
        for (Integer courseId : courseIds) {
            Course course = courseDao.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + courseId));
            Enroll enroll = new Enroll();
            enroll.setStudent(existingStudent);
            enroll.setCourse(course);
            enroll.setEnroll_date(LocalDateTime.now());
            updatedCourses.add(enroll);
        }
        existingStudent.setCourses(updatedCourses);
        studentDao.save(existingStudent);
        return "redirect:/student/home";
    }

}
