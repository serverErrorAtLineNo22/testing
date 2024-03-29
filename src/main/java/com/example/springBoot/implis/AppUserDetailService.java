package com.example.springBoot.implis;


import com.example.springBoot.daos.AdminDao;
import com.example.springBoot.daos.StudentDao;
import com.example.springBoot.daos.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailService implements UserDetailsService {
    @Autowired
    UserDao userDao;
    @Autowired
    StudentDao studentDao;
    @Autowired
    private AdminDao adminDao;
    private User user;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<com.example.springBoot.models.User> userEntityOptional = userDao.findOneByName(name);
        Optional<com.example.springBoot.models.Student> studentEntityOptional = studentDao.findOneByName(name);
        Optional<com.example.springBoot.models.Admin> adminEntityOptional= adminDao.findOneByName(name);

        if (userEntityOptional.isPresent()) {
            com.example.springBoot.models.User user = userEntityOptional.get();
            User.UserBuilder userBuilder = User.withUsername(name)
                    .password(user.getPassword())
                    .authorities(AuthorityUtils.createAuthorityList(user.getRole().name()));

            return userBuilder.build();
        }

        else if (studentEntityOptional.isPresent()) {
            com.example.springBoot.models.Student student = studentEntityOptional.get();
            User.UserBuilder userBuilder = User.withUsername(name)
                    .password(student.getPassword()) // Assuming student also has password field
                    .authorities(AuthorityUtils.createAuthorityList("STUDENT"));

            return userBuilder.build();
        }
        else if (adminEntityOptional.isPresent()){
            com.example.springBoot.models.Admin admin=adminEntityOptional.get();
            User.UserBuilder userBuilder= User.withUsername(name)
                    .password(admin.getPassword())
                    .authorities(AuthorityUtils.createAuthorityList("ADMIN"));
            return userBuilder.build();
        }
        else {
            throw new UsernameNotFoundException("There is no user or student with userName %s".formatted(name));
        }
    }
    }




