package com.app.services.impl;

import com.app.entity.Course;
import com.app.enums.CourseType;
import com.app.enums.StudyMode;
import com.app.repositories.CourseRepository;
import com.app.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> searchPost(StudyMode studyMode, CourseType course) {

        if(course == null && studyMode == null){
            throw new RuntimeException("At least one search criteria must be provided");
        }
        Specification<Course> spec = Specification.where(null);

        if (course != null && studyMode != null) {
            spec = spec.and(CourseSpecificationImpl.nameContains(studyMode));

        }
        if (course != null && studyMode != null) {
            spec = spec.and(CourseSpecificationImpl.searchByCourse(course));
        }

        return courseRepository.findAll(spec);
    }}