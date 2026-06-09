package com.jobboard.job_board.Application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationResponseDto {
    private Long id;
    private String status;
    private String resumeUrl;
    private LocalDateTime appliedAt;
    private Long jobId;
    private String jobTitle;
    private Long userId;
    private String userName;
    private String company;

}
