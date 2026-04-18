package com.jobboard.job_board.job;

import com.jobboard.job_board.company.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Column(nullable = false)
    private BigDecimal salary;

    private String jobtype;     // Full-time / Part-time / Remote

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")            //owning side
    private Company company;
}

