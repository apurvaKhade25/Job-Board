package com.jobboard.job_board.Users;


import com.jobboard.job_board.Application.Application;
import jakarta.persistence.*;
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
    private Long user_id;

    @Column(nullable = false, unique = true)
    private  String email;

    @Column(name = "full_name",nullable = false)
    private String fullname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_At")
    private LocalDateTime created_at;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    @OneToMany(
            mappedBy = "users",     //The other(application) side already manages this relationship
            cascade = CascadeType.ALL,      //save user then save application too
            orphanRemoval = true,           //Orphan = child without parent
            fetch = FetchType.LAZY

    )
    private List <Application> applications=new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.created_at = LocalDateTime.now(); // ✅ auto-set on save
    }

}
