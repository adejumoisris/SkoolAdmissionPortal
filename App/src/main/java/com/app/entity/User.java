package com.app.entity;

import com.app.enums.Gender;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String phoneNumber;
    private String nationality;
    private String countryOfResidence;
    private String countryOfBirth;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

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
