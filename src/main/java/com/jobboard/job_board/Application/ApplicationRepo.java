package com.jobboard.job_board.Application;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {

    //all application by a specific user
    @EntityGraph(attributePaths = {"job", "job.company", "users"})
    List<Application> findByUsersId(Long userId);

    // all applications for a specific job

    @EntityGraph(attributePaths = {"job", "job.company", "users"})
    List<Application> findByJobId(Long jobId);

    // check if user already applied to a job
    boolean existsByUsersIdAndJobId(Long userId, Long jobId);
}
