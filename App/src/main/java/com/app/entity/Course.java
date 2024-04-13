package com.app.entity;

import com.app.enums.CourseType;
import com.app.enums.StudyMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    @Enumerated(value = EnumType.STRING)
    private CourseType courseType;
    @Enumerated(value = EnumType.STRING)
    private StudyMode study;
    private Integer entryYear;
    private Integer entryMonth;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false)
    @LastModifiedDate
    private Date modifiedAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false)
    @LastModifiedDate
    private Date deletedAt;
    private boolean deleted = false;
}
