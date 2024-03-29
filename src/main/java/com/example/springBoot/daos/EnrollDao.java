package com.example.springBoot.daos;

import com.example.springBoot.models.Enroll;
import com.example.springBoot.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollDao extends JpaRepository<Enroll,Integer> {
    void deleteByStudentId(int id);
    List<Enroll> findOneByStudentId(int id);
}
