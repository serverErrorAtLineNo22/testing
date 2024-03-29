package com.example.springBoot.controllers;


import com.example.springBoot.daos.AdminDao;
import com.example.springBoot.models.Admin;
import com.example.springBoot.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminDao adminDao;


    public adminController(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model){
        model.addAttribute("admin",new Admin());
        return "/Admin/adminRegistration";
    }

    @PostMapping("/register")
    public String addAdmin(@ModelAttribute Admin admin){
        MultipartFile photo =admin.getFile();
        if(photo != null && !photo.isEmpty()){
            try {
                byte[] photoBytes = photo.getBytes();
                admin.setPhoto(photoBytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(User.Role.ADMIN);
        adminDao.save(admin);
        return "main/login";
    }

    @GetMapping("/home")
    public String homePage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name= auth.getName();
        Admin admin=adminDao.findByName(name);
        String base64Photo = Base64.getEncoder().encodeToString(admin.getPhoto());
        model.addAttribute("admin",admin);
        model.addAttribute("base64Photo",base64Photo);
        return "Admin/adminHome";
    }
}
