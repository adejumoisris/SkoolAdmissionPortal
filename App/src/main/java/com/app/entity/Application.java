package com.app.entity;

import com.app.enums.Disability;
import com.app.enums.FundingInformation;
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
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String personalStatement;
    @OneToOne
    @JoinColumn(name = "qualification_id")
    private Qualification qualification;
    private boolean academicsReference;
    private boolean employmentDetails;
    private FundingInformation fundingInformation;
    private Disability disability;
    private byte[] passportPhotograph;
    private boolean englishLanguageQualification;

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
