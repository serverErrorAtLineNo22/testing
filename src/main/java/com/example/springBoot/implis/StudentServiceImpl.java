package com.example.springBoot.implis;

import com.example.springBoot.daos.StudentDao;
import com.example.springBoot.models.Student;
import com.example.springBoot.models.User;
import com.example.springBoot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public int findLatestStudentId() {
        List<Student> allStudents=studentDao.findAll();
        int latestStudentId=allStudents.stream().
                mapToInt(Student::getId).
                max().
                orElse(0);
        return latestStudentId;
    }

    @Override
    public String generateStringStudentId(int latestStudentId) {
        if (latestStudentId == 0) {
            return "STU_001";
        } else {
            return String.format("STU_%03d", latestStudentId + 1);
        }
    }
    @Override
    public String getTheStringStudentId(int id){
        return String.format("STU_%03d", id);
    }
    @Override
    @Transactional
    public Student createStudent(Student student) {
    String encodedPassword = passwordEncoder.encode(student.getPassword());
    student.setPassword(encodedPassword);
    student.setRole(User.Role.STUDENT);
        return studentDao.save(student);
    }



}
