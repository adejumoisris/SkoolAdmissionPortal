package com.app.controller;
import com.app.entity.Course;
import com.app.enums.CourseType;
import com.app.enums.StudyMode;
import com.app.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseservice;

    @Autowired
    public CourseController(CourseService courseservice) {
        this.courseservice = courseservice;
    }


    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(
            @RequestParam(required = false) StudyMode studyMode,
            @RequestParam(required = false) CourseType course) {


        List<Course> matchingCourses = courseservice.searchPost(studyMode, course);

        return ResponseEntity.ok(matchingCourses);
    }
}

