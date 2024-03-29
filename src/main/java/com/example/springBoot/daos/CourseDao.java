package com.example.springBoot.daos;

import com.example.springBoot.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDao extends JpaRepository<Course,Integer> {

    List<Course> findByUserId(int id);
}
