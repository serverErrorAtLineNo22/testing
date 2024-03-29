package com.example.springBoot.daos;

import com.example.springBoot.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentDao extends JpaRepository<Student,Integer> {
    Student findByName(String name);
    Student findByEmail(String email);
    Optional<Student> findOneByName(String name);
    List<Student> findByCoursesCourseNameContainingIgnoreCase(String query);
    List<Student> findByNameContainingIgnoreCase(String query);
}
