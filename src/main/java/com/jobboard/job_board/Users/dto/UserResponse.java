package com.jobboard.job_board.Users.dto;

import com.jobboard.job_board.Application.Status;
import com.jobboard.job_board.Users.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserResponse {
    @Email
    private String email;

    private Long user_id;


    private String fullname;

    private Role role;

}
