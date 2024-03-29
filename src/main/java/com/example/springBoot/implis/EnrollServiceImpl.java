package com.example.springBoot.implis;

import com.example.springBoot.daos.EnrollDao;
import com.example.springBoot.models.Enroll;
import com.example.springBoot.models.Student;
import com.example.springBoot.services.EnrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class EnrollServiceImpl implements EnrollService {
    @Autowired
    private EnrollDao enrollDao;
    @Override
    @Transactional
    public void deleteByStudent(int id) {
        enrollDao.deleteByStudentId(id);
    }

    @Override
    @Transactional
   public List<Enroll> findOneByStudentId(int id){
        List<Enroll> enrollList = enrollDao.findOneByStudentId(id);
        return enrollList;
    }

}
