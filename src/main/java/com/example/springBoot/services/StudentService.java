package com.example.springBoot.services;

import com.example.springBoot.models.Course;
import com.example.springBoot.models.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface StudentService {
    int findLatestStudentId();
    String generateStringStudentId(int latestStudentId);
    @Transactional
    Student createStudent(Student student);
    String getTheStringStudentId(int id);

}
