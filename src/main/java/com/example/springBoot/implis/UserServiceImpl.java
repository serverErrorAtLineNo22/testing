package com.example.springBoot.implis;

import com.example.springBoot.daos.UserDao;
import com.example.springBoot.models.User;
import com.example.springBoot.services.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.UnsupportedEncodingException;
import java.util.List;
import net.bytebuddy.utility.RandomString;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public int findLatestUserId() {
        List<User> allUsers = userDao.findAll();
        int latestUserId = allUsers.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0);

        return latestUserId;
    }
    public String generateStringUserId(int latestUserId) {
        if (latestUserId == 0) {
            return "USR_001";
        } else {
            return String.format("USR_%03d", latestUserId + 1);
        }
    }

    @Override
    @Transactional
    public User createUser(User user)  {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(false);
        String randomcode=RandomString.make(64);
        user.setVerificationCode(randomcode);
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ONLINE);
        return userDao.save(user);

    }

    @Override
    @Async
    public void sendVerificationEmail(User user,String siteUrl)
            throws MessagingException, UnsupportedEncodingException {
        String subject ="Please verify your registration.";
        String senderName = "AI admin";
        String mailContent ="<p>Dear "+user.getName()+",</p>";
        mailContent += "<p>Please click the link below to verify to your registration:</p>";
        String verifyURL = siteUrl + "/user/verify?code=" + user.getVerificationCode();
        mailContent += "<h3><a href=\"" + verifyURL + "\" >VERIFY</a></h3>";

        mailContent += "<p>Thank you <br> The Student Management Team.</p>";


        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);

        helper.setFrom("aungzarni571@gmail.com",senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent,true);

        mailSender.send(message);
    }

    @Override
    @Transactional
    public boolean verify(String verificationCode){
        User user =userDao.findByVerificationCode(verificationCode);
        if(user == null || user.isEnabled()){
            return false;
        }
        else {
            userDao.enable(user.getId());
            return true;
        }
    }

    @Override
    public void disconnect(User user){
        var storedUser = userDao.findById(user.getId()).orElse(null);
        if(storedUser != null){
            storedUser.setStatus(User.Status.OFFLINE);
            userDao.save(storedUser);
        }
    }

    @Override
    public List<User> findConnectedUsers(){
        return userDao.findAllByStatus(User.Status.ONLINE);
    }
    @Override
    public String getTheStringUserId(int id){
        return  String.format("USR_%03d",id);
    }
}
