package com.app.services.impl;

import com.app.entity.Course;
import com.app.enums.CourseType;
import com.app.enums.StudyMode;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecificationImpl {
    public static Specification<Course> searchByCourse(CourseType course) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("course"), course);
    }
    public static Specification<Course> nameContains(StudyMode studyMode){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("studyMode"), "%" + studyMode + "%"));
    }
}

