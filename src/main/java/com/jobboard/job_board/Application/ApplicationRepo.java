package com.jobboard.job_board.Application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {

    //all application by a specific user
    List<Application> findByUserId(Long userId);

    // all applications for a specific job
    List<Application> findByJobId(Long jobId);

    // check if user already applied to a job
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
