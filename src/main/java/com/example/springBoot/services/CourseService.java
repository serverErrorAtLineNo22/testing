package com.example.springBoot.services;

import com.example.springBoot.models.Course;

public interface CourseService {
    int getLatestCourseId();

    String generateStringCourseId(int latestId);

    Course addCourse(Course course);
    String getStringCourseId(int id);
}
