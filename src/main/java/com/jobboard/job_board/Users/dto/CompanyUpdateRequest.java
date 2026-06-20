package com.jobboard.job_board.Users.dto;

import lombok.Data;

@Data
public class CompanyUpdateRequest {

    private String name;        // all optional
    private String email;
    private String website;
    private String location;
}
