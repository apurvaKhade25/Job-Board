package com.jobboard.job_board.Users.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private String fullName;

    private String email;

    private String password;


}
