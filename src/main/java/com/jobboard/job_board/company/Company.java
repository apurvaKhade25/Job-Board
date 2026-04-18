package com.jobboard.job_board.company;
import com.jobboard.job_board.job.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String website;
    private String location;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)       //inverse side
    private List<Job> jobs = new ArrayList<>();


}
