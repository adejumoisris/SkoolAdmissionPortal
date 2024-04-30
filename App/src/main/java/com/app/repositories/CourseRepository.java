package com.app.repositories;

import com.app.entity.Application;
import com.app.entity.Course;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAll(Specification<Course> spec);

}
