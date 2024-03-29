package com.example.springBoot.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_data")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Transient
    @Column(length = 15)
    private String stringUser_id;
    @Column(length = 30)
    private String name,email,gender;
    @Column(length = 15)
    private  String phone;
    private String password;
    @Column(name = "verification_code", updatable = false)
    private String verificationCode;

    private boolean enabled;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Transient
    private MultipartFile file;
    private String base64Photo;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status{
        ONLINE,OFFLINE;
    }
    public enum Role{
        ADMIN,USER,STUDENT;
    }
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
}
