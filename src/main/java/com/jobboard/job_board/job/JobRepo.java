package com.jobboard.job_board.job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepo extends JpaRepository<Job,Long> {
        List <Job> findByCompanyId(Long company_id);

}
