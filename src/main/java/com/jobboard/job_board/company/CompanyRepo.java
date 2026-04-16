package com.jobboard.job_board.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepo extends JpaRepository<Company,Long> {
    Optional <Company> findById(Long id);       //default present
    Optional <Company> findByEmail(String email);
}
