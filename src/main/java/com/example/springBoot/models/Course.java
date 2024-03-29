package com.example.springBoot.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "course_data")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 30,name = "course_name")
    private String name;
    @Transient
    @Column(length = 15)
    private String stringCourse_id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    private String status;
    @Column(columnDefinition = "TINYINT")
    private Boolean enabled;

    @OneToMany(mappedBy = "course", cascade ={CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Enroll> studentCourse = new HashSet<>();
    @PrePersist
    public void prePersist() {
        if (this.enabled == null) {
            enabled = true;
        }
        if(this.status == null){
            status = "publish";
        }
    }

}
