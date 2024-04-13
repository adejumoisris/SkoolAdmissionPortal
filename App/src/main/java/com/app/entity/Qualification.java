package com.app.entity;

import com.app.enums.QualificationType;
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
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String institutionName;
    private String areaOfStudy;
    private Integer graduationYear;
    private Double cgpa;
    private QualificationType qualificationType;
    private String countryOfInstitution;

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
