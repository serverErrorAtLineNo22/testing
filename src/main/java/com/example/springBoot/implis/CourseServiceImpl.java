package com.example.springBoot.implis;

import com.example.springBoot.daos.CourseDao;
import com.example.springBoot.models.Course;
import com.example.springBoot.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Override
    public int getLatestCourseId() {
        int latestStudentId=courseDao.findAll().stream().
                mapToInt(Course::getId).
                max().
                orElse(0);
        return latestStudentId;
    }

    @Override
    public String generateStringCourseId(int latestId) {
        if (latestId == 0) {
            return "CSR_001";
        } else {
            return String.format("CSR_%03d", latestId + 1);
        }
    }
    @Override
    public String getStringCourseId(int id) {
            return String.format("CSR_%03d", id );

    }
    @Override
    public Course addCourse(Course course) {
        course.setName(course.getName());
        return courseDao.save(course);
    }
}
