package com.example.springBoot.services;

import com.example.springBoot.models.Enroll;
import com.example.springBoot.models.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EnrollService {

    void deleteByStudent(int id);

    List<Enroll> findOneByStudentId(int id);
}
