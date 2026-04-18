package com.jobboard.job_board.job.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class JobRequestDTO {
    private String title;
    private String description;
    private String location;
    private BigDecimal salary;
    private String jobType;


    private  Long companyId;
}


//title        → "Backend Engineer"
//description  → "We are looking for..."
//location     → "Pune / Remote"
//salary       → 80000
//jobType      → "Full-time"
//companyId    → 1   (which company is posting this job)