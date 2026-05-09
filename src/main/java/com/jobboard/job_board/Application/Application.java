package com.jobboard.job_board.Application;

import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.job.Job;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Application")
@Entity
@Getter
@Setter
public class Application {

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status; //applied,pending,rejected

    private String resumeUrl;

    private LocalDateTime applied_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;
}
