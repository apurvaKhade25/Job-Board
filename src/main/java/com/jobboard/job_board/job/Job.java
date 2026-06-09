package com.jobboard.job_board.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jobboard.job_board.Application.Application;
import com.jobboard.job_board.Skills.Skill;
import com.jobboard.job_board.company.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {
//    id, title, description, location, salary, jobType (Full-time/Part-time/Remote)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 35)
    private String location;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    private String jobtype;     // Full-time / Part-time / Remote

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus=JobStatus.OPEN;

    @PrePersist
    public void onCreate() {
        if (this.jobStatus == null) {
            this.jobStatus = JobStatus.OPEN;   //  always OPEN when created
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({"jobs"}) //owning side
    private Company company;

    @ManyToMany
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skill = new ArrayList<>();



    @OneToMany(
            mappedBy = "job",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Application> applications = new ArrayList<>();
}

