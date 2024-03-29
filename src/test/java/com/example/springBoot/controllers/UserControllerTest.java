package com.example.springBoot.controllers;

import com.example.springBoot.daos.CourseDao;
import com.example.springBoot.daos.UserDao;
import com.example.springBoot.models.Course;
import com.example.springBoot.models.User;
import com.example.springBoot.services.CourseService;
import com.example.springBoot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private CourseService courseService;

    @MockBean
    private CourseDao courseDao;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void userHomePage() throws Exception {
        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock other dependencies
        when(userDao.findByName(any())).thenReturn(new User());
        when(userService.getTheStringUserId(any(Integer.class))).thenReturn("1");
        when(courseService.getLatestCourseId()).thenReturn(1);
        when(courseService.generateStringCourseId(1)).thenReturn("C1");

        mockMvc.perform(get("/user/home")
                        .with(user("testUser").roles("USER"))) // Provide authentication details
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("user_id"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("stringCourseId"))
                .andExpect(view().name("User/userHome"));
    }
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void updateUser_ValidDetails_ShouldSucceed() throws Exception {
        // Mock existing user
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("ExistingUser");
        existingUser.setEmail("existing@example.com");
        existingUser.setPhone("1234567890");
        existingUser.setGender("Male");
        // Mock uploaded photo
        MultipartFile photo = mock(MultipartFile.class);
        when(photo.isEmpty()).thenReturn(false);
        when(photo.getBytes()).thenReturn(new byte[0]);

        // Mock course details
        List<Integer> courseIds = Collections.singletonList(1);
        List<String> courseNames = Collections.singletonList("New Course Name");

        // Mock user and course retrieval from database
        when(userDao.findById(1)).thenReturn(java.util.Optional.of(existingUser));
        when(courseDao.save(any(Course.class))).thenReturn(null);

        mockMvc.perform(post("/user/update")
                        .param("id", "1")
                        .param("name", "UpdatedUser")
                        .param("email", "updated@example.com")
                        .param("phone", "0987654321")
                        .param("gender", "Female")
                        .param("file", "mockPhoto") // Assume the file has been uploaded
                        .param("courseIds", "1")
                        .param("courseNames", "New Course Name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/home"));

        // Verify that user details are updated
        verify(userDao, times(1)).findById(1);
        verify(userDao, times(1)).save(existingUser);

        // Verify that course names are updated
        verify(courseDao, times(1)).save(any(Course.class));
    }
}
