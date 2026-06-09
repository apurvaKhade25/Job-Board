package com.jobboard.job_board.Users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jobboard.job_board.Application.Application;
import com.jobboard.job_board.company.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private  String email;

    @Column(name = "full_name",nullable = false)
    private String fullname;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_At")
    private LocalDateTime created_at;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    @NotNull
    private Role role;

    @Column(name = "resume_url")
    private String resumeUrl;

    @OneToMany(
            mappedBy = "users",     //The other(application) side already manages this relationship
            cascade = CascadeType.ALL,      //save user then save application too
            orphanRemoval = true,           //Orphan = child without parent
            fetch = FetchType.LAZY

    )
    @JsonIgnore
    private List <Application> applications=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;      // recruiter belongs to company

    @PrePersist
    public void onCreate() {
        this.created_at = LocalDateTime.now(); //  auto-set on save
    }

}
