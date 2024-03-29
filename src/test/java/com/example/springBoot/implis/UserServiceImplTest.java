/*
package com.example.springBoot.implis;

import com.example.springBoot.daos.UserDao;
import com.example.springBoot.models.User;
import com.example.springBoot.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreateUser(){
        User user=new User();
        user.setId(1);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhone("1234567890");
        user.setGender("Male");
        user.setPassword("password123");
    when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPhone("1234567890");
        savedUser.setGender("Male");
        savedUser.setPassword("encodedPassword"); // Encoded password
        when(userDao.save(user)).thenReturn(savedUser);
        // Calling the createUser method
        User result = userService.createUser(user);

        // Verifying that password encoding and user saving methods are
        verify(passwordEncoder).encode(eq("password123"));
        verify(userDao).save(any(User.class));

        // Asserting that the returned user matches the expected saved user
        assertEquals(savedUser, result);
    }



}*/
