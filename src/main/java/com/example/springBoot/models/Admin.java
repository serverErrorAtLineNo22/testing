package com.example.springBoot.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
@Table(name = "admin_data")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private String dob;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Transient
    private MultipartFile file;
    private String base64Photo;

    @Enumerated(EnumType.STRING)
    private User.Role role;
    public enum Role{
        ADMIN,USER,STUDENT;
    }
}
