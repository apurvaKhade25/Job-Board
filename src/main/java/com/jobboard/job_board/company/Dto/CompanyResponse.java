package com.jobboard.job_board.company.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyResponse {
    private Long id;
    private String name;
    private String email;
    private String website;
    private String location;
}
