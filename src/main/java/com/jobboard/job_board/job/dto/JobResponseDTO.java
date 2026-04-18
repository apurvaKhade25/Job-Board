package com.jobboard.job_board.job.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private BigDecimal salary;
    private String jobType;
    private Long companyId;
    private String companyName;
}
