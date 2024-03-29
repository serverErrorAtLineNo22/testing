package com.example.springBoot.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student_data")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Transient
    private String stringStudent_id;

    private String name;
    private String email;
    private String password;
    private String dob;
    private String gender;
    private String phone;
    private String education;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Transient
    private MultipartFile file;
    private String base64Photo;

    @Enumerated(EnumType.STRING)
    private User.Role role;
    @OneToMany(mappedBy = "student",fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Enroll> courses = new HashSet<>();

}

