package com.jobboard.job_board.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPageResponse {
    private List <JobResponseDTO> jobs;
    private int currentPage;                // which page you're on
    private int totalPages;                 // total pages available
    private long totalJobs;                 // total jobs in DB
    private boolean isFirst;               // is this page 1?
    private boolean isLast;                // is this the last page?
    private int pageSize;                  // jobs per page

}
