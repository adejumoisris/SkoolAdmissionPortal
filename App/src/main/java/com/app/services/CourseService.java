package com.app.services;

import com.app.entity.Course;
import com.app.enums.CourseType;
import com.app.enums.StudyMode;

import java.util.List;

public interface CourseService {

    List<Course> searchPost(StudyMode studyMode, CourseType course);
}
