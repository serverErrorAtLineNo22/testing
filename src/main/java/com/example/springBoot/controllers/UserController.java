package com.example.springBoot.controllers;


import com.example.springBoot.daos.CourseDao;
import com.example.springBoot.daos.UserDao;
import com.example.springBoot.models.Course;
import com.example.springBoot.models.User;
import com.example.springBoot.models.Utility;
import com.example.springBoot.services.CourseService;
import com.example.springBoot.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseDao courseDao;

    @GetMapping("/home")
    public String userHomePage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userDao.findByName(name);
        if (user != null) {
            String base64Photo = "";
            byte[] photo = user.getPhoto();
            if (photo != null) {
                base64Photo = Base64.getEncoder().encodeToString(photo);
            }
        String stringUserId = userService.getTheStringUserId(user.getId());
        int latestCourseId = courseService.getLatestCourseId();
        String stringCourseId = courseService.generateStringCourseId(latestCourseId);
        List<Course> courses = user.getCourses();
        model.addAttribute("base64Photo",base64Photo);
        model.addAttribute("user",user);
        model.addAttribute("user_id",stringUserId);
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courses);
        model.addAttribute("stringCourseId", stringCourseId);
        return "User/userHome";
    } else

    {
        // Handle the case where user is null, perhaps by redirecting to an error page or login page.
        return "redirect:/";
    }
    }
    @GetMapping(value = "/registration")
    public String showRegistrationForm(Model model) {
        int latestUserId = userService.findLatestUserId();
        String stringUserId = userService.generateStringUserId(latestUserId);
        model.addAttribute("user", new User());
        model.addAttribute("stringUserId", stringUserId);
        return "User/userRegistration";
    }
    @PostMapping(value = "/register")
    public String userRegistration(@ModelAttribute User user, HttpServletRequest request, RedirectAttributes redirectAttributes) throws MessagingException {
        if(userDao.findByName(user.getName())== null && userDao.findByEmail(user.getEmail())==null){
            MultipartFile photo =user.getFile();
            if(photo != null && !photo.isEmpty()){

                byte[] photoBytes = new byte[0];
                try {
                    photoBytes = photo.getBytes();
                    user.setPhoto(photoBytes);
                    userService.createUser(user);
                    String siteUrl= Utility.getSiteUrl(request);
                    userService.sendVerificationEmail(user,siteUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

            return "Main/verifyMail";
        }
        else if(userDao.findByName(user.getName())!=null){
            redirectAttributes.addAttribute("userNameExist",true);
            return "redirect:/user/registration";
        }
        else if(userDao.findByEmail(user.getEmail())!=null){
            redirectAttributes.addAttribute("emailExist",true);
            return "redirect:/user/registration";
        }
        else {
            redirectAttributes.addAttribute("userNameExist", true);
            redirectAttributes.addAttribute("emailExist", true);

            return "redirect:/user/registration";
        }
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public User addUser(
            @Payload User user
    ) {
        userService.createUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/topic")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }



    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User updatedUser,
                             @RequestParam("courseIds") List<Integer> courseIds,
                             @RequestParam("courseNames") List<String> courseNames) {
        User existingUser = userDao.findById(updatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID: " + updatedUser.getId()));

        // Update user details
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setGender(updatedUser.getGender());
        MultipartFile photo = updatedUser.getFile();
        try {
            // Check if a new photo is uploaded
            if (!photo.isEmpty()) {
                byte[] photoBytes = photo.getBytes();
                existingUser.setPhoto(photoBytes);
            }
        } catch (IOException e) {
            // Handle exception
        }

        // Update course names
        List<Course> courses = existingUser.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (courseIds.contains(course.getId())) {
                int index = courseIds.indexOf(course.getId());
                course.setName(courseNames.get(index));
                courseDao.save(course);
            }
        }

        userDao.save(existingUser);
        return "redirect:/user/home";
    }
    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model){
        boolean verified=userService.verify(code);
        String pageTitle = verified ? "Verification Succeeded !" : "Verification Failed";
        model.addAttribute("pageTitle",pageTitle);

        return "Main/"+(verified ?"verify_success":"verify_failed");
    }
}
