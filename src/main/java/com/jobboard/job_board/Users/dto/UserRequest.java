package com.jobboard.job_board.Users.dto;

import com.jobboard.job_board.Users.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullname;

    @NotBlank
    private String password;

    @NotBlank
    private Role role;
}
