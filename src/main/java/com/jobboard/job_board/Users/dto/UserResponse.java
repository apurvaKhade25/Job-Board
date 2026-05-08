package com.jobboard.job_board.Users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserResponse {
    @Email
    private String email;

    private Long user_id;


    private String fullname;

    private String role;

}
