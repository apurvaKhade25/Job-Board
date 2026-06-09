package com.jobboard.job_board.Application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status; //applied,pending,rejected

    private String resumeUrl;

    private LocalDateTime appliedAt;

    @PrePersist
    public void onApply(){
        this.appliedAt=LocalDateTime.now();
        if (this.status==null){
             this.status = ApplicationStatus.PENDING;
        }
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"applications", "password","created_at","role"})
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @JsonIgnoreProperties({"applications","company","skill","skills"})
    private Job job;

}
