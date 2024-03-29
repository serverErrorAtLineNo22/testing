package com.example.springBoot.controllers;

import com.example.springBoot.daos.StudentDao;
import com.example.springBoot.daos.UserDao;
import com.example.springBoot.models.User;
import com.example.springBoot.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class pageController {

    @Autowired
    private final UserDao userDao;
    @Autowired
    private final StudentDao studentDao;
    @Autowired
    private StudentService studentService;

    public pageController(UserDao userDao,
                          StudentDao studentDao) {
        this.userDao = userDao;
        this.studentDao = studentDao;
    }

    @GetMapping("/home")
    public String mainPage() {
        return "Main/home";
    }
    @GetMapping("/")
    public String loginPage(){
        return "Main/login";
    }

    @GetMapping("/login-success")
    public String loginSuccess(HttpSession session) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return "redirect:/admin/home";
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("STUDENT"))) {
            return "redirect:/student/home";
        } else if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(("USER")))) {
            User user = userDao.findByName(auth.getName());
            String stringUserId =studentService.getTheStringStudentId(user.getId());
            session.setAttribute("id",user.getId());
            session.setAttribute("name", user.getName());
            session.setAttribute("user_id", stringUserId);
            return "redirect:/user/home";
        }
        else {
            return "redirect:/login-failure";
        }

    }

    @GetMapping("/login-failure")
    public String loginFailure(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("error", true);
        return "redirect:/login?error=true";
    }
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}


   /* @GetMapping("/")
    String homePage(){
        var auth= SecurityContextHolder.getContext().getAuthentication();
        if(auth !=null && auth.getAuthorities().stream().
                anyMatch(a-> a.getAuthority().equals(User.Role.ADMIN.name())
                        ||  a.getAuthority().equals(User.Role.USER.name())
                        || a.getAuthority().equals(User.Role.STUDENT.name()))){
            return "/Main/home";
        }
        else{
            return "/Main/login";}
    }
    @GetMapping("/login")
    public String home(){
        return "Main/login";
    }

    @PostMapping(value = "/login")
    public String LoginPagePost(@RequestParam("loginName") String loginName, @RequestParam ("loginPassword")String loginPassword, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = userDao.findByUserName(loginName);

        if (user != null && user.getPassword().equals(loginPassword)) {
           HttpSession session = request.getSession();
            session.setAttribute("LoginUser", user.getId());
            return "User/userHome";
        } else {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/login";
        }
        }*/


