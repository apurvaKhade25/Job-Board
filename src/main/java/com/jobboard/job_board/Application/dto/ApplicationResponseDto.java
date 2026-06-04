package com.jobboard.job_board.Application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class ApplicationResponseDto {
    private Long id;
    private String status;
    private String resumeUrl;
    private LocalDate appliedAt;
    private Long jobId;
    private String jobTitle;
    private Long userId;
    private String userName;
    private String company;

}
