package com.jobboard.job_board.job.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CursorResponse {
    private List<JobResponseDTO> jobs;
    private Long nextCursor;
    private int pageSize;
    private boolean hasMore;
}
