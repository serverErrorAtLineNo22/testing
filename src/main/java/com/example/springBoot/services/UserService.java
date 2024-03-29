package com.example.springBoot.services;

import com.example.springBoot.models.User;
import jakarta.mail.MessagingException;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    int findLatestUserId();
    String generateStringUserId(int latestUserId);
    @Transactional
   User createUser(User user) ;
    String getTheStringUserId(int id);
    void sendVerificationEmail(User user,String siteUrl) throws MessagingException, UnsupportedEncodingException;
    @Transactional
    boolean verify(String verificationCode);

    void disconnect(User user);

    List<User> findConnectedUsers();
}
