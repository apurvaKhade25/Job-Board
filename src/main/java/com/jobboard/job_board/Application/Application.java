package com.jobboard.job_board.Application;

import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.job.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Application")
@Entity
public class Application {

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status; //applied,pending,rejected

    private String resume_url;

    private LocalDateTime applied_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
